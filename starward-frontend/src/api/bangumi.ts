import apiClient from './client';
import type { BangumiCollectionItem, BangumiSummary } from '@/types';

export interface BangumiQueryParams {
  subjectType?: number;
  collectionType?: number;
  keyword?: string;
  sort?: string;
}

export const getBangumiCollections = (params?: BangumiQueryParams): Promise<BangumiCollectionItem[]> => {
  return apiClient.get('/v1/bangumi', { params });
};

export const getBangumiSummary = (): Promise<BangumiSummary> => {
  return apiClient.get('/v1/bangumi/summary');
};

export const syncBangumi = (userId?: string): Promise<any> => {
  return apiClient.post('/v1/bangumi/sync', null, { params: { userId } });
};
