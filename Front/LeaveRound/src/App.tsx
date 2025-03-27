import { MenuBar } from '@/components/common/MenuBar';
import { Route, Routes, useLocation } from 'react-router-dom';
import { AccountDonate } from './pages/AccountDonatePage.tsx';
import { AccountDetail } from './pages/AccountDetailPage.tsx';
import Onboarding from './pages/OnboardingPage.tsx';
import Login from './pages/LoginPage.tsx';

function App() {
  const location = useLocation();

  // MenuBar를 숨길 경로들
  const hideMenuBarPaths = ['/', '/login'];

  // 현재 경로가 해당 배열에 포함되는지 확인
  const shouldShowMenuBar = !hideMenuBarPaths.includes(location.pathname);

  return (
    <div className='min-h-screen bg-background'>
      <div className='App relative flex flex-col min-h-screen'>
        <main className='flex-1 pb-[5rem]'>
          <Routes>
            <Route path='/' element={<Onboarding />} />
            <Route path='/login' element={<Login />} />
            <Route path='/:userId/donate' element={<AccountDonate />} />
            <Route path='/:userId/donate/:id' element={<AccountDetail />} />
          </Routes>
        </main>
        
        {/* 조건부 렌더링 */}
        {shouldShowMenuBar && <MenuBar />}
      </div>
    </div>
  );
}

export default App;
