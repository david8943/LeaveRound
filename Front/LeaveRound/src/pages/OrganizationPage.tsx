import React from "react";
import LinkIcon from "../assets/icons/Link.svg";
import LoveHandIcon from "../assets/icons/LoveHand.svg";

const categories = [
    "전체", "🌱 환경", "🤝 보건", "📚 교육", "🎸 기타"
];

const charityList = [
    {
        name: "초록우산",
        description: "아동의 권리를 보호하고 복지를 증진하는 전문 아동복지기관입니다.",
        amount: 10000000,
    },
    {
        name: "굿네이버스",
        description: "국내 및 해외 취약 계층 아동을 위한 글로벌 NGO 단체입니다.",
        amount: 5000000,
    },
    {
        name: "굿네이버스",
        description: "국내 및 해외 취약 계층 아동을 위한 글로벌 NGO 단체입니다.",
        amount: 5000000,
    },
    {
        name: "굿네이버스",
        description: "국내 및 해외 취약 계층 아동을 위한 글로벌 NGO 단체입니다.",
        amount: 5000000,
    },
    {
        name: "굿네이버스",
        description: "국내 및 해외 취약 계층 아동을 위한 글로벌 NGO 단체입니다.",
        amount: 5000000,
    },
];

interface CharityCardProps {
    name: string;
    description: string;
    amount: number;
}

const OrganizationPage = () => {
    const CharityCard: React.FC<CharityCardProps> = ({ name, description, amount }) => {
        return (
            <div className="w-[380px] h-[172px] bg-[#FCF2D6] rounded-lg p-[23px] flex flex-col border border-primary">
                {/* 기부처 이름 & 링크 아이콘 */}
                <div className="flex items-center mb-4">
                    <h3 className="font-bold inline-flex items-center gap-1">
                        {name}
                        <img src={LinkIcon} alt="링크" className="w-5 h-5" />
                    </h3>
                </div>

                {/* 기부처 설명 */}
                <p className="text-body text-gray-600 mb-4">{description}</p>

                {/* 모금 금액 & 아이콘 */}
                <div className="flex items-center justify-end mt-auto">
                    <img src={LoveHandIcon} alt="기부 아이콘" className="w-6 h-6 mr-2" />
                    <span className="font-heading text-heading">
                        {amount.toLocaleString()}
                    </span>
                    <span className="text-body ml-1">원</span>
                </div>
            </div>
        );
    };

    return (
        <div className="w-[412px] mx-auto bg-background">
            {/* 헤더 */}
            <header className="h-[62px] flex items-center justify-center font-heading text-heading">
                기부처
            </header>

            {/* 설명 문구 */}
            <p className="text-center text-text-deepgray">
                민들레가 선정한 기부처는 AI를 통해<br />
                검증된 신뢰있는 기관입니다.
            </p>

            {/* 카테고리 */}
            <div className="mt-4 flex justify-center space-x-2">
                {categories.map((category, index) => (
                    <button key={index} className="bg-primary-light px-3 pt-1.5 pb-1 rounded-[1rem] text-sm">
                        {category}
                    </button>
                ))}
            </div>

            {/* 기부단체 리스트 */}
            <div className="my-4 flex flex-col items-center space-y-4">
                {charityList.map((charity, index) => (
                    <CharityCard key={index} {...charity} />
                ))}
            </div>
        </div>
    );
};

export default OrganizationPage;