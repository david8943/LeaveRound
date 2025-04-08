import { useState, useEffect, useCallback } from 'react';
import { AxiosRequestConfig, AxiosError, Method } from 'axios';
import api from '@/services/api';

interface UseAxiosProps {
  url: string;
  method?: Method;
  data?: any;
  config?: AxiosRequestConfig;
  executeOnMount?: boolean;
}

interface UseAxiosReturn<T> {
  response: T | null;
  error: AxiosError | null;
  loading: boolean;
  refetch: (overrideData?: any) => Promise<void>;
}

const useAxios = <T = any>({
  url,
  method = 'get',
  data = null,
  config = {},
  executeOnMount = true,
}: UseAxiosProps): UseAxiosReturn<T> => {
  const [response, setResponse] = useState<T | null>(null);
  const [error, setError] = useState<AxiosError | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const fetchData = useCallback(
    async (overrideData: any = data) => {
      if (loading) return; // 이미 로딩 중이면 중복 호출 방지

      setLoading(true);
      setError(null);

      try {
        const res = await api<T>({
          url,
          method,
          data: overrideData,
          ...config,
        });
        setResponse(res.data);
      } catch (err) {
        setError(err as AxiosError);
      } finally {
        setLoading(false);
      }
    },
    [url, method, data, config, loading],
  );

  useEffect(() => {
    if (executeOnMount && !loading && !response) {
      fetchData();
    }
  }, [executeOnMount, fetchData, loading, response]);

  return { response, error, loading, refetch: fetchData };
};

export default useAxios;
