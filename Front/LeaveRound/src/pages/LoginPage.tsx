import React, { useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import SeedLeaveRound from '@/assets/icons/SeedLeaveRoundSmall.svg';
import TitleLayout from '@/components/layout/TitleLayout';
import Modal from '@/components/Modal';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';
import { useAuthStore } from '@/stores/useAuthStore';

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorType, setErrorType] = useState<'invalid-email' | 'login-fail' | ''>('');
  const [showModal, setShowModal] = useState(false);

  const passwordInputRef = useRef<HTMLInputElement>(null);
  const { setIsLoggedIn } = useAuthStore.getState();
  const navigator = useNavigate();

  const isValidEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const { response, error, refetch } = useAxios<{ isSuccess: boolean }>({
    url: API.member.login,
    method: 'post',
    executeOnMount: false,
  });

  useEffect(() => {
    if (email === '') {
      setErrorType('');
    } else if (!isValidEmail(email)) {
      setErrorType('invalid-email');
    } else if (errorType === 'invalid-email') {
      setErrorType('');
    }
  }, [email]);

  const isLoginDisabled = !isValidEmail(email) || email === '' || password === '';

  const submitLogin = () => {
    const form = new FormData();
    form.append('email', email);
    form.append('password', password);
    refetch(form);
  };

  useEffect(() => {
    if (error) {
      setErrorType('login-fail');
      setShowModal(true);
      setPassword('');
      passwordInputRef.current?.focus();
      return;
    }

    if (response?.isSuccess) {
      navigator('/main');
      sessionStorage.setItem('isLoggedIn', 'true');
      setIsLoggedIn(true);
    } else if (response && !response.isSuccess) {
      setErrorType('login-fail');
      setShowModal(true);
      setPassword('');
      passwordInputRef.current?.focus();
    }
  }, [response, error]);

  return (
    <TitleLayout title='로그인'>
      <div className='flex flex-col items-center w-full'>
        <img src={SeedLeaveRound} alt='로고' className='mt-[94px] w-[76px] h-[76px]' />
        <p className='mt-[26px] text-body text-center'>민들레와 함께 선한 영향력을 후-</p>
        <div className='relative flex flex-col items-center w-full px-[50px]'>
          <div className='mt-[110px] w-full'>
            <input
              type='email'
              placeholder='이메일'
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className='w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]'
            />
            <div className='h-[16px] mt-[13px]'>
              {errorType === 'invalid-email' && (
                <p className='text-detail text-primary'>이메일 형식을 지켜주세요.</p>
              )}
            </div>
          </div>
          <div className='mt-[47px] w-full'>
            <input
              ref={passwordInputRef}
              type='password'
              placeholder='비밀번호'
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className='w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]'
            />
            <div className='h-[32px] mt-[13px]'>
              {errorType === 'login-fail' && (
                <p className='text-detail text-primary leading-[1.1rem]'>
                  아이디 또는 비밀번호가 잘못되었습니다.
                  <br />
                  아이디와 비밀번호를 정확히 입력해 주세요.
                </p>
              )}
            </div>
          </div>
          <div className='mt-[190px] w-full flex justify-center'>
            <button
              onClick={submitLogin}
              disabled={isLoginDisabled}
              className={`w-[312px] h-[48px] rounded-[24px] text-white ${isLoginDisabled ? 'bg-[#E0E0E0] cursor-not-allowed' : 'bg-primary'
                }`}
            >
              로그인
            </button>
          </div>
          <p className='mt-[26px] text-detail'>
            아직 회원이 아니신가요?{' '}
            <Link to='/signup' className='text-primary'>
              회원가입
            </Link>
          </p>
        </div>
      </div>

      {showModal && (
        <Modal
          mainMessage="로그인 실패"
          detailMessage="아이디 또는 비밀번호를 확인해 주세요."
          onClose={() => setShowModal(false)}
        />
      )}
    </TitleLayout>
  );
};

export default LoginPage;
