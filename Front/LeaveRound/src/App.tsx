import { MenuBar } from '@/components/common/MenuBar';
import { Route, Routes } from 'react-router-dom';
import { AccountDonate } from './pages/AccountDonatePage.tsx';
import { AccountDetail } from './pages/AccountDetailPage.tsx';
import { HomePage } from './pages/HomePage.tsx';

function App() {
  return (
    <div className='min-h-screen bg-background'>
      <div className='App relative flex flex-col min-h-screen'>
        <main className='flex-1 pb-[5rem]'>
          <Routes>
            {/* 임시 라우터 */}
            <Route path='/' element={<HomePage />} />
            <Route path='/:userId/donate' element={<AccountDonate />} />
            <Route path='/:userId/donate/:id' element={<AccountDetail />} />
          </Routes>
        </main>

        <MenuBar />
      </div>
    </div>
  );
}

export default App;
