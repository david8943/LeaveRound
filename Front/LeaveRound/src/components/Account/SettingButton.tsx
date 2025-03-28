interface SettingButtonProps {
  text: string;
  isSelected: boolean;
  onClick: () => void;
}

export const SettingButton = ({ text, isSelected, onClick }: SettingButtonProps) => {
  return (
    <button
      onClick={onClick}
      className={`w-auto h-[30px] px-[1rem] mt-[0.5rem] mb-[1.5rem]  rounded-[16px] transition-colors
        ${isSelected ? 'bg-primary font-semibold ' : 'bg-primary-light'}`}
    >
      {text}
    </button>
  );
};
