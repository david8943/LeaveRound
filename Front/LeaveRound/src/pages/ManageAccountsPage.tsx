import React, { useEffect, useState } from "react";
import TitleLayout from "@/components/layout/TitleLayout";
import AccountCard from "@/components/Account/AccountPreview";
import { getBankIcon } from "@/constants/bankIconMap";
import useAxios from "@/hooks/useAxios";
import { API } from "@/constants/url";
import Modal from "@/components/Modal";
import api from "@/services/api";

type Account = {
  autoDonationId: number | null;
  bankName: string;
  accountNo: string;
  accountMoney: string;
  accountStatus: "AUTO_ENABLED" | "AUTO_PAUSED" | "AUTO_DISABLED";
};

const ManageAccountsPage: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [totalAmount, setTotalAmount] = useState<number>(0);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedAutoDonationId, setSelectedAutoDonationId] = useState<number | null>(null);

  const {
    response: accountResponse,
    refetch: refetchAccounts,
  } = useAxios<{ result: Account[] }>({
    url: API.member.account,
    method: "get",
    executeOnMount: false,
  });

  const {
    response: donationResponse,
    refetch: refetchTotalDonation,
  } = useAxios<{ result: { totalAccount: number } }>({
    url: API.autoDonation.totalAmount,
    method: "get",
    executeOnMount: false,
  });

  useEffect(() => {
    refetchAccounts();
    refetchTotalDonation();
  }, []);

  useEffect(() => {
    if (accountResponse?.result) {
      setAccounts(accountResponse.result);
    }
  }, [accountResponse]);

  useEffect(() => {
    if (donationResponse?.result?.totalAccount != null) {
      setTotalAmount(donationResponse.result.totalAccount);
    }
  }, [donationResponse]);

  const sortedAccounts = [...accounts].sort((a, b) => {
    const priority = {
      AUTO_ENABLED: 0,
      AUTO_PAUSED: 1,
      AUTO_DISABLED: 2,
    };
    return priority[a.accountStatus] - priority[b.accountStatus];
  });

  const openModal = (autoDonationId: number | null) => {
    if (autoDonationId) {
      setSelectedAutoDonationId(autoDonationId);
      setIsModalOpen(true);
    }
  };

  const closeModal = () => {
    setSelectedAutoDonationId(null);
    setIsModalOpen(false);
  };

  const handleDelete = async () => {
    if (!selectedAutoDonationId) return;

    try {
      await api.delete(API.autoDonation.detail(String(selectedAutoDonationId)));
      closeModal();
      refetchAccounts();
      refetchTotalDonation();
    } catch (err) {
      console.error("자동기부 삭제 실패", err);
    }
  };

  return (
    <TitleLayout title="계좌 관리">
      <div className="px-[32px] pt-[36px]">
        <p className="text-detail text-right">총 기부금액</p>
        <p className="text-[36px] text-right mt-[12px] font-heading">
          {totalAmount.toLocaleString()} 원
        </p>

        <p className="mt-[45px] text-body">전체 계좌</p>

        <div className="mt-[16px] space-y-[16px] pb-[5rem]">
          {sortedAccounts.map((acc) => (
            <AccountCard
              key={acc.accountNo}
              bankIcon={getBankIcon(acc.bankName)}
              bankName={acc.bankName}
              accountNumber={acc.accountNo}
              balance={Number(acc.accountMoney)}
              accountStatus={acc.accountStatus}
              showButton
              onClickButton={() => {
                if (acc.accountStatus !== "AUTO_DISABLED") {
                  openModal(acc.autoDonationId); // - 버튼만 동작
                }
              }}
            />
          ))}
        </div>
      </div>

      {isModalOpen && (
        <Modal
          mainMessage="자동기부 설정을 삭제하겠습니까?"
          detailMessage="삭제 후 복구는 불가합니다."
          onClose={closeModal}
          onConfirm={handleDelete}
        />
      )}
    </TitleLayout>
  );
};

export default ManageAccountsPage;
