import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { createIssue } from '../../services/issueService';
import { getAllCategories } from '../../services/categoryService';
import { uploadMultipleFiles } from '../../services/fileService';
import { PRIORITY, FILE_UPLOAD } from '../../utils/constants';
import { formatFileSize, isValidFileType, isValidFileSize } from '../../utils/helpers';
import LoadingSpinner from '../../components/LoadingSpinner';
import toast from 'react-hot-toast';

const CreateIssue = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [uploadingFiles, setUploadingFiles] = useState(false);
  
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    categoryId: '',
    location: {
      building: '',
      floor: '',
      room: ''
    },
    priority: 'MEDIUM',
    attachments: []
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await getAllCategories();
      setCategories(response.data || []);
    } catch (error) {
      toast.error('Failed to load categories');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    
    if (name.startsWith('location.')) {
      const locationField = name.split('.')[1];
      setFormData(prev => ({
        ...prev,
        location: {
          ...prev.location,
          [locationField]: value
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
    
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const handleFileSelect = (e) => {
    const files = Array.from(e.target.files);
    
    // Validate file count
    if (files.length + selectedFiles.length > FILE_UPLOAD.MAX_FILES) {
      toast.error(`Maximum ${FILE_UPLOAD.MAX_FILES} files allowed`);
      return;
    }
    
    // Validate each file
    const validFiles = [];
    for (const file of files) {
      if (!isValidFileType(file, FILE_UPLOAD.ALLOWED_TYPES)) {
        toast.error(`${file.name}: Invalid file type. Only images allowed.`);
        continue;
      }
      if (!isValidFileSize(file, FILE_UPLOAD.MAX_SIZE)) {
        toast.error(`${file.name}: File too large. Max ${formatFileSize(FILE_UPLOAD.MAX_SIZE)}`);
        continue;
      }
      validFiles.push(file);
    }
    
    setSelectedFiles(prev => [...prev, ...validFiles]);
  };

  const removeFile = (index) => {
    setSelectedFiles(prev => prev.filter((_, i) => i !== index));
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.title.trim()) {
      newErrors.title = 'Title is required';
    }
    if (!formData.description.trim()) {
      newErrors.description = 'Description is required';
    }
    if (!formData.categoryId) {
      newErrors.categoryId = 'Category is required';
    }
    if (!formData.location.building.trim()) {
      newErrors['location.building'] = 'Building is required';
    }
    if (!formData.location.floor.trim()) {
      newErrors['location.floor'] = 'Floor is required';
    }
    if (!formData.location.room.trim()) {
      newErrors['location.room'] = 'Room is required';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      toast.error('Please fill in all required fields');
      return;
    }
    
    setLoading(true);
    
    try {
      let attachmentUrls = [];
      
      // Upload files if any
      if (selectedFiles.length > 0) {
        setUploadingFiles(true);
        const uploadResponse = await uploadMultipleFiles(selectedFiles);
        attachmentUrls = uploadResponse.data || [];
        setUploadingFiles(false);
      }
      
      // Create issue with attachment URLs
      const issueData = {
        ...formData,
        attachments: attachmentUrls
      };
      
      const response = await createIssue(issueData);
      
      toast.success('Issue created successfully!');
      navigate(`/employee/issues/${response.data.id}`);
    } catch (error) {
      console.error('Error creating issue:', error);
      toast.error(error.response?.data?.message || 'Failed to create issue');
    } finally {
      setLoading(false);
      setUploadingFiles(false);
    }
  };

  return (
    <div className="space-y-6 max-w-4xl">
      <div>
        <h1 className="text-3xl font-bold mb-2">Create New Issue</h1>
        <p className="text-gray-600">Report a new issue or problem</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Title */}
        <div>
          <label className="label">
            Title <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className={`input ${errors.title ? 'border-red-500' : ''}`}
            placeholder="Brief summary of the issue"
          />
          {errors.title && <p className="text-red-500 text-sm mt-1">{errors.title}</p>}
        </div>

        {/* Description */}
        <div>
          <label className="label">
            Description <span className="text-red-500">*</span>
          </label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="6"
            className={`input ${errors.description ? 'border-red-500' : ''}`}
            placeholder="Detailed description of the issue..."
          ></textarea>
          {errors.description && (
            <p className="text-red-500 text-sm mt-1">{errors.description}</p>
          )}
        </div>

        {/* Category and Priority */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="label">
              Category <span className="text-red-500">*</span>
            </label>
            <select
              name="categoryId"
              value={formData.categoryId}
              onChange={handleChange}
              className={`input ${errors.categoryId ? 'border-red-500' : ''}`}
            >
              <option value="">Select category</option>
              {categories.map(cat => (
                <option key={cat.id} value={cat.id}>
                  {cat.name}
                </option>
              ))}
            </select>
            {errors.categoryId && (
              <p className="text-red-500 text-sm mt-1">{errors.categoryId}</p>
            )}
          </div>

          <div>
            <label className="label">
              Priority <span className="text-red-500">*</span>
            </label>
            <select
              name="priority"
              value={formData.priority}
              onChange={handleChange}
              className="input"
            >
              <option value={PRIORITY.LOW}>⬇️ Low</option>
              <option value={PRIORITY.MEDIUM}>➡️ Medium</option>
              <option value={PRIORITY.HIGH}>⬆️ High</option>
              <option value={PRIORITY.CRITICAL}>🔥 Critical</option>
            </select>
          </div>
        </div>

        {/* Location */}
        <div>
          <h3 className="text-lg font-semibold mb-4">Location</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="label">
                Building <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                name="location.building"
                value={formData.location.building}
                onChange={handleChange}
                className={`input ${errors['location.building'] ? 'border-red-500' : ''}`}
                placeholder="e.g., Main Building"
              />
              {errors['location.building'] && (
                <p className="text-red-500 text-sm mt-1">{errors['location.building']}</p>
              )}
            </div>

            <div>
              <label className="label">
                Floor <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                name="location.floor"
                value={formData.location.floor}
                onChange={handleChange}
                className={`input ${errors['location.floor'] ? 'border-red-500' : ''}`}
                placeholder="e.g., 2"
              />
              {errors['location.floor'] && (
                <p className="text-red-500 text-sm mt-1">{errors['location.floor']}</p>
              )}
            </div>

            <div>
              <label className="label">
                Room <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                name="location.room"
                value={formData.location.room}
                onChange={handleChange}
                className={`input ${errors['location.room'] ? 'border-red-500' : ''}`}
                placeholder="e.g., Lab 3"
              />
              {errors['location.room'] && (
                <p className="text-red-500 text-sm mt-1">{errors['location.room']}</p>
              )}
            </div>
          </div>
        </div>

        {/* File Upload */}
        <div>
          <label className="label">Attachments (Optional)</label>
          <div className="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
            <input
              type="file"
              id="file-upload"
              multiple
              accept={FILE_UPLOAD.ALLOWED_EXTENSIONS.join(',')}
              onChange={handleFileSelect}
              className="hidden"
            />
            <label htmlFor="file-upload" className="cursor-pointer">
              <div className="text-gray-600">
                <svg
                  className="mx-auto h-12 w-12 text-gray-400"
                  stroke="currentColor"
                  fill="none"
                  viewBox="0 0 48 48"
                >
                  <path
                    d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                    strokeWidth={2}
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
                <p className="mt-2">Click to upload images</p>
                <p className="text-sm text-gray-500">
                  Max {FILE_UPLOAD.MAX_FILES} files, {formatFileSize(FILE_UPLOAD.MAX_SIZE)} each
                </p>
              </div>
            </label>
          </div>

          {/* Selected Files */}
          {selectedFiles.length > 0 && (
            <div className="mt-4 space-y-2">
              {selectedFiles.map((file, index) => (
                <div
                  key={index}
                  className="flex items-center justify-between bg-gray-50 p-3 rounded"
                >
                  <div className="flex items-center gap-2">
                    <svg
                      className="w-5 h-5 text-gray-500"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z"
                      />
                    </svg>
                    <span className="text-sm">{file.name}</span>
                    <span className="text-xs text-gray-500">
                      ({formatFileSize(file.size)})
                    </span>
                  </div>
                  <button
                    type="button"
                    onClick={() => removeFile(index)}
                    className="text-red-500 hover:text-red-700"
                  >
                    <svg
                      className="w-5 h-5"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M6 18L18 6M6 6l12 12"
                      />
                    </svg>
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Submit Buttons */}
        <div className="flex gap-4 pt-4">
          <button
            type="submit"
            disabled={loading || uploadingFiles}
            className="btn btn-primary flex-1"
          >
            {loading ? (
              <span className="flex items-center justify-center">
                <LoadingSpinner size="sm" />
                <span className="ml-2">
                  {uploadingFiles ? 'Uploading files...' : 'Creating issue...'}
                </span>
              </span>
            ) : (
              'Create Issue'
            )}
          </button>
          <button
            type="button"
            onClick={() => navigate('/employee/my-issues')}
            className="btn btn-secondary"
            disabled={loading}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateIssue;
