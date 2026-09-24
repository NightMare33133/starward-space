import axios from 'axios';
import type { Result } from '@/types';

const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 响应拦截器：自动解构 Result<T>，并统一防御未知异常
apiClient.interceptors.response.use(
  (response) => {
    const res = response.data as Result<any>;
    // 业务状态码 200 表示成功
    if (res.code === 200) {
      return res.data;
    }
    // 业务异常统一抛出错误信息
    const errorMsg = res.message || '系统星际通讯出现异常';
    console.warn(`[API Business Warning] ${errorMsg}`);
    return Promise.reject(new Error(errorMsg));
  },
  (error) => {
    const msg = error.response?.data?.message || error.message || '网络连接失败，请检查服务状态';
    console.error(`[API Network Error] ${msg}`);
    return Promise.reject(new Error(msg));
  }
);

export default apiClient;
