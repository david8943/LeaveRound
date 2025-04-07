import AccountModal from './AccountModal';
import { useEffect, useState } from 'react';
import { BasicButton } from '../common/BasicButton';
import { TOrganization } from '@/models/organizations';
import { API } from '@/constants/url';
import useAxios from '@/hooks/useAxios';

interface OrganizationModalProps {
  onClose: () => void;
  onSave: (purpose: string) => void;
  selectedId: (id: number) => void;
  currentPurpose?: string;
}

const CATEGORIES = [
  { id: 'all', label: '전체', emoji: '❓' },
  { id: 'environment', label: '환경', emoji: '🌱' },
  { id: 'health', label: '보건', emoji: '🧑‍🤝‍🧑' },
  { id: 'education', label: '교육', emoji: '📚' },
  { id: 'etc', label: '기타', emoji: '🎸' },
];

type OrganizationListResponse = {
  result: {
    organizationInfos: TOrganization[];
  };
};

export const OrganizationModal = ({ onClose, onSave, selectedId, currentPurpose }: OrganizationModalProps) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [selectedOrganization, setSelectedOrganization] = useState(currentPurpose || '리브라운드가 정해주세요!');

  const { response, refetch } = useAxios<OrganizationListResponse>({
    url: API.organization.list,
    executeOnMount: false,
  });

  useEffect(() => {
    refetch();
  }, []);

  const organizations = response?.result?.organizationInfos ?? [];
  const selectedLabel = CATEGORIES.find((cat) => cat.id === selectedCategory)?.label;

  const filteredOrganizations = organizations.filter((org) => {
    const matchesSearch = org.organizationName.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = selectedLabel === '전체' || org.projectCategory === selectedLabel;
    return matchesSearch && matchesCategory;
  });

  const handleSave = () => {
    onSave(selectedOrganization);
    onClose();
  };

  return (
    <AccountModal onClose={onClose}>
      <div className='flex flex-col items-center w-full'>
        <div className='w-full mb-4'>
          <p className='flex justify-center items-center mb-4'>기부처를 지정해주세요!</p>

          <input
            type='text'
            placeholder='검색어를 입력하세요'
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className='w-full px-4 py-2 border border-gray-300 rounded-full focus:outline-none focus:border-primary'
          />
        </div>

        <div className='flex gap-2 mb-4 overflow-x-auto w-full'>
          {CATEGORIES.map((category) => (
            <button
              key={category.id}
              onClick={() => setSelectedCategory(category.id)}
              className={`px-4 py-2 rounded-full whitespace-nowrap ${
                selectedCategory === category.id ? 'bg-primary text-white' : 'bg-primary-light'
              }`}
            >
              {category.emoji} {category.label}
            </button>
          ))}
        </div>

        <div className='w-full mb-4 space-y-2 max-h-[300px] overflow-y-auto'>
          {filteredOrganizations?.map((org: TOrganization) => (
            <button
              key={org.organizationId}
              onClick={() => {
                selectedId(org.organizationProjectId);
                setSelectedOrganization(org.organizationName);
              }}
              className={`w-full px-4 py-[1rem] rounded-[0.5rem] border border-primary text-left ${
                selectedOrganization === org.organizationName
                  ? 'bg-[rgba(255,217,95,0.8)]'
                  : 'bg-[rgba(255,217,95,0.2)]'
              }`}
            >
              {CATEGORIES.find((cat) => cat.id === org.projectCategory)?.emoji}
              {org.organizationName}
            </button>
          ))}
        </div>
        <div className='flex gap-2 mt-4'>
          <BasicButton text='저장하기' onClick={handleSave} className='w-[313px] h-[48px]' />
        </div>
      </div>
    </AccountModal>
  );
};
