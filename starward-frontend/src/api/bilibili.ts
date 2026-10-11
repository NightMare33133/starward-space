import apiClient from './client';
import type { BilibiliBangumi, BilibiliSummary } from '@/types';

export interface BilibiliQueryParams {
  type?: string;
  keyword?: string;
  sort?: string;
}

export const getBilibiliList = (params?: BilibiliQueryParams): Promise<BilibiliBangumi[]> => {
  return apiClient.get('/v1/bilibili', { params });
};

export const getBilibiliSummary = (): Promise<BilibiliSummary> => {
  return apiClient.get('/v1/bilibili/summary');
};

export const syncBilibili = (vmid?: string): Promise<any> => {
  return apiClient.post('/v1/bilibili/sync', null, { params: { vmid } });
};
