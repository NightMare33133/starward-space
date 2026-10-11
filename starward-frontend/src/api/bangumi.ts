import apiClient from './client';
import type { Bangumi, BangumiSummary } from '@/types';

export interface BangumiQueryParams {
  type?: string;
  keyword?: string;
  sort?: string;
}

export const getBangumiList = (params?: BangumiQueryParams): Promise<Bangumi[]> => {
  return apiClient.get('/v1/bangumi', { params });
};

export const getBangumiSummary = (): Promise<BangumiSummary> => {
  return apiClient.get('/v1/bangumi/summary');
};

export const syncBangumi = (vmid?: string): Promise<any> => {
  return apiClient.post('/v1/bangumi/sync', null, { params: { vmid } });
};
