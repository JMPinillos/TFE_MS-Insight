import { fileRepository } from '../data/persistence/InjectionDependencyRepository';

export const uploadFiles = async (type: 'fixed' | 'variable', formData: FormData) => {
  if (type === 'fixed') {
    return await fileRepository.uploadFixedData(formData);
  } else {
    return await fileRepository.uploadVariableData(formData);
  }
};
