// hooks/useAxios.ts
import { useState, useEffect, useCallback } from 'react';
import axios, { AxiosRequestConfig, AxiosError, Method } from 'axios';

interface UseAxiosProps<T> {
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
}: UseAxiosProps<T>): UseAxiosReturn<T> => {
  const [response, setResponse] = useState<T | null>(null);
  const [error, setError] = useState<AxiosError | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const baseURL = import.meta.env.VITE_BASE_URL;
  const fullURL = `${baseURL}${url}`;

  const fetchData = useCallback(
    async (overrideData: any = data) => {
      setLoading(true);
      setError(null);

      try {
        const res = await axios<T>({
          url: fullURL,
          method,
          data: overrideData,
          withCredentials: true,
          ...config,
        });
        setResponse(res.data);
      } catch (err) {
        setError(err as AxiosError);
      } finally {
        setLoading(false);
      }
    },
    [fullURL, method, data, config],
  );

  useEffect(() => {
    if (executeOnMount) {
      fetchData();
    }
  }, [fetchData, executeOnMount]);

  return { response, error, loading, refetch: fetchData };
};

export default useAxios;
