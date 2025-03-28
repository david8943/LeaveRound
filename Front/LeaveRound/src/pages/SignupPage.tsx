import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import SeedLeaveRound from '@/assets/icons/SeedLeaveRoundSmall.svg';
import TitleLayout from '@/components/layout/TitleLayout';

const SignupPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [isEmailValid, setIsEmailValid] = useState(true);
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newEmail = e.target.value;
    setEmail(newEmail);
    setIsEmailValid(/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(newEmail));
  };

  const isSignupDisabled =
    !isEmailValid || email === '' || name === '' || password === '';

  return (
    <TitleLayout title="회원가입">
      <div className="flex flex-col items-center w-full">
        {/* 아이콘 및 문구 */}
        <img src={SeedLeaveRound} alt="로고" className="mt-[94px] w-[76px] h-[76px]" />
        <p className="mt-[26px] text-body text-center">민들레와 함께 선한 영향력을 후-</p>

        <div className="flex flex-col items-center w-full px-[50px]">
          {/* 이메일 입력 */}
          <div className="mt-[110px] w-full">
            <input
              type="email"
              placeholder="이메일"
              value={email}
              onChange={handleEmailChange}
              className="w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]"
            />
            <div className="h-[16px] mt-[6px]">
              {!isEmailValid && (
                <p className="text-detail text-primary">이메일 형식을 지켜주세요</p>
              )}
            </div>
          </div>

          {/* 이름 입력 */}
          <div className="mt-[54px] w-full">
            <input
              type="text"
              placeholder="이름"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]"
            />
          </div>

          {/* 비밀번호 입력 */}
          <div className="mt-[76px] w-full">
            <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]"
            />
          </div>

          {/* 회원가입 버튼 */}
          <div className="mt-[125px] w-full flex justify-center">
            <button
              disabled={isSignupDisabled}
              className={`w-[312px] h-[48px] rounded-[24px] text-white ${isSignupDisabled
                ? 'bg-[#E0E0E0] cursor-not-allowed'
                : 'bg-primary'
                }`}
            >
              회원가입
            </button>
          </div>

          {/* 로그인 링크 */}
          <p className="mt-[26px] text-detail">
            이미 계정이 있으신가요?{' '}
            <Link to="/login" className="text-primary">
              로그인
            </Link>
          </p>
        </div>
      </div>
    </TitleLayout>
  );
};

export default SignupPage;
