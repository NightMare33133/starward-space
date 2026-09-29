import { ref } from 'vue';

export interface ToastItem {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  duration?: number;
}

const toasts = ref<ToastItem[]>([]);

export const useToast = () => {
  const show = (message: string, type: ToastItem['type'] = 'info', duration = 3000) => {
    const id = Math.random().toString(36).substring(2, 9);
    toasts.value.push({ id, type, message, duration });

    if (duration > 0) {
      setTimeout(() => {
        remove(id);
      }, duration);
    }
    return id;
  };

  const remove = (id: string) => {
    const index = toasts.value.findIndex(t => t.id === id);
    if (index !== -1) {
      toasts.value.splice(index, 1);
    }
  };

  const success = (message: string, duration = 3000) => show(message, 'success', duration);
  const error = (message: string, duration = 3500) => show(message, 'error', duration);
  const warning = (message: string, duration = 3000) => show(message, 'warning', duration);
  const info = (message: string, duration = 3000) => show(message, 'info', duration);

  return {
    toasts,
    show,
    remove,
    success,
    error,
    warning,
    info,
  };
};
