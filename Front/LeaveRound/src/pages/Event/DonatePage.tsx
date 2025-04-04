import { BasicButton } from '@/components/common/BasicButton';
import Logo from '@/assets/Logo.png';
import BtnRightArrow from '@/assets/icons/btn-right-arrow.svg';
import WhiteDandelion from '@/assets/white-dandelion.png';
import YellowDandelion from '@/assets/yellow-dandelion.png';
import { useEffect, useState } from 'react';
import useAxios from '@/hooks/useAxios';
import { API } from '@/constants/url';
import { User } from '@/models/member';
import { OrganizationModal } from '@/components/Account/OrganizationModal';
import { createPortal } from 'react-dom';

const DonatePage = () => {
  const [userInfo, setUserInfo] = useState<User>();
  const [whiteCnt, setWhiteCnt] = useState<number>();
  const [yellowCnt, setYellowCnt] = useState<number>();
  const [selectedOrganizationId, setSelectedOrganizationId] = useState<number>();
  const [selectedPurpose, setSelectedPurpose] = useState<string>('기부처 랜덤');
  const [isOrganizationModalOpen, setIsOrganizationModalOpen] = useState<boolean>(false);

  const { response, refetch } = useAxios<{ result: User }>({
    url: API.member.signup,
    method: 'get',
    executeOnMount: false,
  });

  const { refetch: donate } = useAxios({
    url: API.event.donateDandelion,
    method: 'post',
    executeOnMount: false,
  });

  useEffect(() => {
    refetch();
  }, []);

  useEffect(() => {
    if (!response?.result) return;
    setUserInfo(response.result);
  }, [response]);

  const handleSelectOrganization = () => {
    setIsOrganizationModalOpen(true);
  };

  const handleOrganizationSave = (purpose: string) => {
    setSelectedPurpose(purpose === '리브라운드가 정해주세요!' ? '기부처 랜덤' : purpose);
    setIsOrganizationModalOpen(false);
  };

  const handleDonate = async () => {
    if (!selectedOrganizationId) return alert('기부처를 지정해주세요');
    if (whiteCnt <= 0 && yellowCnt <= 0) return alert('기부할 민들레를 입력해주세요');

    await donate({
      dandelionCount: whiteCnt,
      goldDandelionCount: yellowCnt,
      projectId: selectedOrganizationId,
    });

    alert('기부가 완료되었습니다!');
  };

  return (
    <div className='h-[calc(100%-62px)] flex flex-col justify-center gap-20'>
      <div className='flex flex-col justify-center items-center gap-4'>
        <img className='w-[100px] h-[100px]' src={Logo} />
        <div>민들레 홀씨로 선한 영향력을 퍼뜨려 보세요.</div>
      </div>

      <div className='flex justify-center'>
        <div className='flex flex-col gap-6'>
          <div className='flex gap-4'>
            <div className='font-bold'>기부한 홀씨</div>
            <div>{userInfo?.totalDonationCount}개</div>
          </div>
          <div className='flex gap-4 items-center'>
            <div className='font-bold'>현재 기부처</div>
            <div>{selectedPurpose}</div>
            <div>
              <button
                onClick={handleSelectOrganization}
                className='flex items-center justify-center gap-[4px] w-[80px] h-[30px] bg-primary-light text-detail rounded-[4px]'
              >
                <div className='pt-[1px]'>지정하기</div>
                <img src={BtnRightArrow} className='w-[10px] h-[10px]' />
              </button>
            </div>
          </div>
        </div>
      </div>
      <div className='flex flex-col items-center gap-6'>
        <div className='flex items-center gap-1'>
          <img className='w-[40px]' src={WhiteDandelion} />
          <div>
            <input
              type='number'
              value={whiteCnt}
              onChange={(e) => setWhiteCnt(Number(e.target.value))}
              placeholder={`${userInfo?.dandelionCount ?? ''}`}
              className='placeholder-gray-400 px-1 text-center bg-transparent w-[46px] border-b-2 border-gray-600 mr-1'
            />
            <span>개</span>
          </div>
        </div>

        <div className='flex items-center gap-1'>
          <img className='w-[40px]' src={YellowDandelion} />
          <div>
            {userInfo?.goldDandelionCount ? (
              <div>
                <input
                  type='number'
                  value={yellowCnt}
                  onChange={(e) => setYellowCnt(Number(e.target.value))}
                  placeholder={`${userInfo?.goldDandelionCount ?? ''}`}
                  className='bg-transparent w-[46px] border-b-2 border-gray-500 mr-1'
                />
                <span>개</span>
              </div>
            ) : (
              <div className='text-gray-400'>황금 민들레가 없어요!</div>
            )}
          </div>
        </div>
      </div>

      <div className='px-10'>
        <BasicButton text='기부하기' onClick={handleDonate} />
      </div>

      {isOrganizationModalOpen &&
        createPortal(
          <OrganizationModal
            onClose={() => setIsOrganizationModalOpen(false)}
            onSave={handleOrganizationSave}
            selectedId={setSelectedOrganizationId}
            currentPurpose={selectedPurpose === '기부처 랜덤' ? '리브라운드가 정해주세요!' : selectedPurpose}
          />,
          document.body,
        )}
    </div>
  );
};

export default DonatePage;
