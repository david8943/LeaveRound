import AccountModal from './AccountModal';
import { useState } from 'react';
import { BasicButton } from '../common/BasicButton';

interface OrganizationModalProps {
  onClose: () => void;
  onSave: (purpose: string) => void;
  currentPurpose?: string;
}

const CATEGORIES = [
  { id: 'all', label: '전체', emoji: '❓' },
  { id: 'environment', label: '환경', emoji: '🌱' },
  { id: 'health', label: '보건', emoji: '🧑‍🤝‍🧑' },
  { id: 'education', label: '교육', emoji: '📚' },
  { id: 'etc', label: '기타', emoji: '🎸' },
];

const ORGANIZATIONS = [
  { id: 'random', name: '리브라운드가 정해주세요!', category: 'all' },
  { id: 'eco1', name: '초록우산', category: 'environment' },
  { id: 'eco2', name: '싸피복지관', category: 'environment' },
  { id: 'edu1', name: 'IT교육기관', category: 'education' },
  { id: 'etc1', name: '국립문화센터', category: 'etc' },
  { id: 'etc2', name: '문화체육관광부', category: 'etc' },
];

export const OrganizationModal = ({ onClose, onSave, currentPurpose }: OrganizationModalProps) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [selectedOrganization, setSelectedOrganization] = useState(currentPurpose || '리브라운드가 정해주세요!');

  const filteredOrganizations = ORGANIZATIONS.filter((org) => {
    const matchesSearch =
      org.name.toLowerCase().startsWith(searchTerm.toLowerCase()) ||
      org.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = selectedCategory === 'all' || org.category === selectedCategory;
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
          {filteredOrganizations.map((org) => (
            <button
              key={org.id}
              onClick={() => setSelectedOrganization(org.name)}
              className={`w-full px-4 py-[1rem] rounded-[0.5rem] border border-primary text-left ${
                selectedOrganization === org.name ? 'bg-[rgba(255,217,95,0.8)]' : 'bg-[rgba(255,217,95,0.2)]'
              }`}
            >
              {CATEGORIES.find((cat) => cat.id === org.category)?.emoji}
              {org.name}
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
