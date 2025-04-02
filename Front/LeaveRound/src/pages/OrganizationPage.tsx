import React, { useEffect, useState } from "react";
import LinkIcon from "../assets/icons/Link.svg";
import LoveHandIcon from "../assets/icons/LoveHand.svg";
import { API } from "../constants/url";
import useAxios from "../hooks/useAxios";

interface OrganizationInfo {
    organizationId: number;
    organizationName: string;
    homepageUrl: string | null;
    goalAmount: number;
    currentAmount: number;
    projectContent: string;
}

const categories = [
    { label: "전체", value: undefined },
    { label: "🌱 환경", value: "환경" },
    { label: "🤝 보건", value: "보건" },
    { label: "📚 교육", value: "교육" },
    { label: "🎸 기타", value: "기타" },
];

const OrganizationPage = () => {
    const [selectedCategory, setSelectedCategory] = useState<string | undefined>(undefined);

    const { response, loading, error, refetch } = useAxios<any>({
        url: API.organization.list,
        method: "get",
        config: {
            params: selectedCategory ? { projectCategory: selectedCategory } : {},
        },
        executeOnMount: false, // 초기 자동 요청 방지
    });

    useEffect(() => {
        refetch(); // 카테고리 변경 시 수동 요청
    }, [selectedCategory]);

    const organizations: OrganizationInfo[] =
        response?.result?.organizationInfos?.sort((a: OrganizationInfo, b: OrganizationInfo) =>
            a.organizationName.localeCompare(b.organizationName, "ko")
        ) || [];

    const CharityCard: React.FC<OrganizationInfo> = ({ organizationName, homepageUrl, goalAmount, currentAmount, projectContent }) => {
        const percentage = goalAmount ? Math.floor((currentAmount / goalAmount) * 100) : 0;

        return (
            <div className="w-[380px] h-[195px] bg-[#FCF2D6] rounded-lg p-[23px] flex flex-col border border-primary">
                {/* 이름 & 아이콘 */}
                <div className="flex items-center mb-2">
                    <h3 className="font-bold inline-flex items-center gap-1">
                        {organizationName}
                        {homepageUrl && (
                            <img
                                src={LinkIcon}
                                alt="링크"
                                className="w-5 h-5 cursor-pointer"
                                onClick={() => window.open(homepageUrl, "_blank", "noopener noreferrer")}
                            />
                        )}
                    </h3>
                </div>

                {/* 설명 */}
                <p className="text-body text-gray-600 mb-2 line-clamp-2">{projectContent}</p>

                {/* 퍼센트 바 */}
                <div className="w-full h-2 bg-gray-300 rounded-full overflow-hidden mb-2">
                    <div className="h-full bg-primary transition-all duration-300" style={{ width: `${percentage}%` }}></div>
                </div>

                {/* 퍼센트 텍스트와 금액 */}
                <div className="flex items-center justify-between mt-auto">
                    <span className="text-body">{`${percentage}%`}</span>
                    <div className="flex items-center">
                        <img src={LoveHandIcon} alt="기부 아이콘" className="w-6 h-6 mr-2" />
                        <span className="font-heading text-heading">{currentAmount.toLocaleString()}</span>
                        <span className="text-body ml-1">원</span>
                    </div>
                </div>
            </div>
        );
    };

    return (
        <div className="w-[412px] mx-auto bg-background pb-20">
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
                {categories.map(({ label, value }) => (
                    <button
                        key={label}
                        className={`px-3 pt-1.5 pb-1 rounded-[1rem] text-sm ${selectedCategory === value || (value === undefined && selectedCategory === undefined)
                            ? "bg-primary"
                            : "bg-primary-light"
                            }`}
                        onClick={() => setSelectedCategory(value)}
                    >
                        {label}
                    </button>
                ))}
            </div>

            {/* 기부단체 리스트 */}
            <div className="my-4 flex flex-col items-center space-y-4">
                {loading && <p>불러오는 중...</p>}
                {error && <p>에러 발생: {error.message}</p>}
                {!loading && !error &&
                    organizations.map((org) => <CharityCard key={org.organizationId} {...org} />)}
            </div>
        </div>
    );
};

export default OrganizationPage;
