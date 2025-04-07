import { useState, useEffect, useCallback } from 'react';
import { AxiosRequestConfig, AxiosError, Method } from 'axios';
import api from '@/services/api';

interface UseAxiosProps {
  url: string;
  method?: Method;
  data?: any;
  config?: AxiosRequestConfig;
  executeOnMount?: boolean;
  withCredentials?: boolean;
}

interface UseAxiosReturn<T> {
  response: T | null;
  error: AxiosError | null;
  loading: boolean;
  refetch: (overrideData?: any) => Promise<T>;
}

const useAxios = <T = any>({
  url,
  method = 'get',
  data = null,
  config = {},
  executeOnMount = true,
  withCredentials = false,
}: UseAxiosProps): UseAxiosReturn<T> => {
  const [response, setResponse] = useState<T | null>(null);
  const [error, setError] = useState<AxiosError | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const fetchData = useCallback(
    async (overrideData: any = data) => {
      setLoading(true);
      setError(null);

      try {
        const res = await api<T>({
          url,
          method,
          data: overrideData,
          ...config,
          withCredentials,
        });
        setResponse(res.data);
        return res.data;
      } catch (err) {
        setError(err as AxiosError);
        throw err;
      } finally {
        setLoading(false);
      }
    },
    [url, method, data, config, withCredentials],
  );

  useEffect(() => {
    if (executeOnMount) {
      fetchData();
    }
  }, [fetchData, executeOnMount]);

  return { response, error, loading, refetch: fetchData };
};

export default useAxios;
