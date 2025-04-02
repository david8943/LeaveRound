import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SeedLeaveRound from '@/assets/icons/SeedLeaveRoundSmall.svg';
import TitleLayout from '@/components/layout/TitleLayout';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<'invalid-email' | 'login-fail' | ''>('');

  const isValidEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const { response, refetch } = useAxios<any>({
    url: API.member.login,
    method: 'post',
    executeOnMount: false,
  });

  useEffect(() => {
    if (email === '') {
      setError('');
    } else if (!isValidEmail(email)) {
      setError('invalid-email');
    } else if (error === 'invalid-email') {
      setError('');
    }
  }, [email]);

  const isLoginDisabled = !isValidEmail(email) || email === '' || password === '';

  const submitLogin = () => {
    const form = new FormData();
    form.append('email', email);
    form.append('password', password);

    refetch(form); // ✅ 요청만 보내고
  };

  // ✅ 응답 처리
  useEffect(() => {
    if (response) {
      console.log('로그인 응답:', response);
      // 여기서 로그인 성공 로직 수행 (예: 리다이렉트, 토큰 저장 등)
    }
  }, [response]);

  return (
    <TitleLayout title='로그인'>
      <div className='flex flex-col items-center w-full'>
        {/* 아이콘 */}
        <img src={SeedLeaveRound} alt='로고' className='mt-[94px] w-[76px] h-[76px]' />
        <p className='mt-[26px] text-body text-center'>민들레와 함께 선한 영향력을 후-</p>

        <div className='relative flex flex-col items-center w-full px-[50px]'>
          {/* 이메일 입력 */}
          <div className='mt-[110px] w-full'>
            <input
              type='email'
              placeholder='이메일'
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className='w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]'
            />
            <div className='h-[16px] mt-[13px]'>
              {error === 'invalid-email' && <p className='text-detail text-primary'>이메일 형식을 지켜주세요.</p>}
            </div>
          </div>

          {/* 비밀번호 입력 */}
          <div className='mt-[47px] w-full'>
            <input
              type='password'
              placeholder='비밀번호'
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className='w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]'
            />
            <div className='h-[32px] mt-[13px]'>
              {error === 'login-fail' && (
                <p className='text-detail text-primary leading-[1.1rem]'>
                  아이디 또는 비밀번호가 잘못되었습니다.
                  <br />
                  아이디와 비밀번호를 정확히 입력해 주세요.
                </p>
              )}
            </div>
          </div>

          {/* 로그인 버튼 */}
          <div className='mt-[190px] w-full flex justify-center'>
            <button
              onClick={submitLogin}
              disabled={isLoginDisabled}
              className={`w-[312px] h-[48px] rounded-[24px] text-white ${
                isLoginDisabled ? 'bg-[#E0E0E0] cursor-not-allowed' : 'bg-primary'
              }`}
            >
              로그인
            </button>
          </div>

          {/* 회원가입 링크 */}
          <p className='mt-[26px] text-detail'>
            아직 회원이 아니신가요?{' '}
            <Link to='/signup' className='text-primary'>
              회원가입
            </Link>
          </p>
        </div>
      </div>
    </TitleLayout>
  );
};

export default LoginPage;
