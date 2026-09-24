import apiClient from './client';
import type { Moment, MomentCreateRequest } from '@/types';

export const getMoments = (): Promise<Moment[]> => {
  return apiClient.get('/v1/moments');
};

export const createMoment = (data: MomentCreateRequest): Promise<Moment> => {
  return apiClient.post('/v1/moments', data);
};
