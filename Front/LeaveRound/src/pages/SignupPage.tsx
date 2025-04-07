import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import SeedLeaveRound from '@/assets/icons/SeedLeaveRoundSmall.svg';
import TitleLayout from '@/components/layout/TitleLayout';
import { API } from '@/constants/url';
import useAxios from '@/hooks/useAxios';
import Modal from '@/components/Modal';

const SignupPage: React.FC = () => {
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [isEmailValid, setIsEmailValid] = useState(true);
  const [name, setName] = useState('');
  const [isNameValid, setIsNameValid] = useState(true);
  const [password, setPassword] = useState('');
  const [isPasswordValid, setIsPasswordValid] = useState(true);
  const [showModal, setShowModal] = useState(false);

  const { response, error, loading, refetch } = useAxios({
    url: API.member.signup,
    method: 'post',
    executeOnMount: false,
  });

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newEmail = e.target.value;
    setEmail(newEmail);
    if (newEmail === '') {
      setIsEmailValid(true);
    } else {
      setIsEmailValid(/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(newEmail));
    }
  };

  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newName = e.target.value;
    setName(newName);
    setIsNameValid(newName.length <= 20);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newPassword = e.target.value;
    setPassword(newPassword);
    setIsPasswordValid(newPassword.length >= 8);
  };

  const isSignupDisabled =
    !isEmailValid ||
    !isNameValid ||
    !isPasswordValid ||
    email === '' ||
    name === '' ||
    password === '';

  const handleSignup = () => {
    if (isSignupDisabled) return;
    refetch({ name, email, password });
  };

  useEffect(() => {
    if (response?.isSuccess) {
      setShowModal(true);
    }
  }, [response]);

  return (
    <TitleLayout title="회원가입">
      <div className="flex flex-col items-center w-full">
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
              {!isEmailValid && email !== '' && (
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
              onChange={handleNameChange}
              className="w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]"
            />
            <div className="h-[16px] mt-[6px]">
              {!isNameValid && (
                <p className="text-detail text-primary">이름은 20자 이하로 입력해주세요</p>
              )}
            </div>
          </div>

          {/* 비밀번호 입력 */}
          <div className="mt-[54px] w-full">
            <input
              type="password"
              placeholder="비밀번호 (8자 이상)"
              value={password}
              onChange={handlePasswordChange}
              className="w-[312px] h-[34px] border-b outline-none bg-transparent border-[var(--text-deepgray)] placeholder-[var(--text-deepgray)]"
            />
            <div className="h-[16px] mt-[6px]">
              {!isPasswordValid && (
                <p className="text-detail text-primary">비밀번호는 최소 8자 이상이어야 해요</p>
              )}
            </div>
          </div>

          {/* 회원가입 버튼 */}
          <div className="mt-[103px] w-full flex justify-center">
            <button
              onClick={handleSignup}
              disabled={isSignupDisabled}
              className={`w-[312px] h-[48px] rounded-[24px] text-white ${isSignupDisabled ? 'bg-[#E0E0E0] cursor-not-allowed' : 'bg-primary'
                }`}
            >
              {loading ? '가입 중...' : '회원가입'}
            </button>
          </div>

          {/* 로그인 링크 */}
          <p className="mt-[26px] text-detail">
            이미 계정이 있으신가요?{' '}
            <Link to="/login" className="text-primary">
              로그인
            </Link>
          </p>

          {error && (
            <p className="text-detail text-alert mt-2">
              오류가 발생했습니다: {error.message}
            </p>
          )}
        </div>
      </div>

      {/* 회원가입 성공 모달 */}
      {showModal && (
        <Modal
          mainMessage="회원가입 완료!"
          detailMessage="로그인 후 민들레를 만나보세요!"
          onClose={() => navigate('/login')}
        />
      )}
    </TitleLayout>
  );
};

export default SignupPage;
