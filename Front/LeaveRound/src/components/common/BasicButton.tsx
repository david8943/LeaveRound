/* BasicButton : 기본 버튼 컴포넌트 */
interface BasicButtonProps {
  text?: string; // 버튼 텍스트
  rounded?: boolean; // 버튼 모서리 둥글게 할지 여부(모달창-true)
  className?: string; // 추가할 클래스를 받을 수 있도록 함, w-[100%] 등
  disabled?: boolean; // 버튼 비활성화 여부
  onClick?: () => void; // 버튼 클릭 시 실행할 함수
}

export const BasicButton = ({
  text = '버튼',
  rounded = true,
  disabled = false,
  className = '',
  onClick,
}: BasicButtonProps) => {
  return (
    <div className={`flex justify-center items-center ${className}`}>
      <button
        disabled={disabled}
        onClick={onClick}
        className={`w-full h-[3rem] text-white bg-[var(--primary)] disabled:bg-[var(--text-disabled)] ${
          rounded ? 'rounded-[1.5rem]' : 'rounded-[0.5rem]'
        } border-none`}
      >
        {text}
      </button>
    </div>
  );
};
