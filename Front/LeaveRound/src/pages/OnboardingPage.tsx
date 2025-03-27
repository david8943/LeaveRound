import React from "react";
import { useNavigate } from "react-router-dom";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Slider from "react-slick";
// import { BasicButton } from "@/components/common/BasicButton";
import LeaveRound from "@/assets/icons/SeedLeaveRoundLarge.svg";
import Card1 from "@/assets/images/OnboardingCard1.svg";
import Card2 from "@/assets/images/OnboardingCard2.svg";
import Card3 from "@/assets/images/OnboardingCard3.svg";

const Onboarding: React.FC = () => {
  const navigate = useNavigate();

  const settings = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    centerMode: true,
    // centerPadding: "0px",
  };
  

  return (
    <div className="bg-background flex flex-col items-center w-[var(--width-base)] mx-auto">
      <img
        src={LeaveRound}
        alt="Leave Round"
        className="w-auto h-auto relative -top-[90px] left-[15px]"
      />

      {/* 텍스트 */}
      <p className="mt-[-170px] text-detail">
        리브라운드와 함께 선한 영향력을 전달해보세요.
      </p>

      {/* 캐러셀 */}
      <Slider {...settings} className="w-full">
        {[Card1, Card2, Card3].map((card, index) => (
          <div key={index}>
            <img
              src={card}
              alt={`Card ${index + 1}`}
              className="mx-auto w-full"
            />
          </div>
        ))}
      </Slider>



      {/* 로그인 버튼 */}
      <div className="absolute bottom-[95px]">
        <button
        className="w-[313px] h-[48px] bg-[var(--primary)] text-white rounded-[24px] text-base font-medium disabled:bg-[var(--text-disabled)]"
        >
        로그인
        </button>
      </div>

      {/* 회원가입 링크 */}
      <p className="absolute bottom-[53px] text-detail">
        아직 회원이 아니신가요?{" "}
        <span
          className="text-primary cursor-pointer"
          onClick={() => navigate("/signup")}
        >
          회원가입
        </span>
      </p>
    </div>
  );
};

export default Onboarding;
