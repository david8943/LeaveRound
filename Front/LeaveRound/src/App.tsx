import { MenuBar } from '@/components/common/MenuBar';
import { Route, Routes, Outlet } from 'react-router-dom';
import { AccountDonate } from './pages/AccountDonatePage.tsx';
import { AccountDetail } from './pages/AccountDetailPage.tsx';
import DonationKingPage from './pages/Event/DonationKingPage.tsx';
import DonatePage from './pages/Event/DonatePage.tsx';
import Onboarding from '@/pages/OnboardingPage.tsx';
import Login from '@/pages/LoginPage.tsx';
import Signup from '@/pages/SignupPage.tsx';
import Organization from '@/pages/OrganizationPage.tsx';
import TitleLayout from '@/components/layout/TitleLayout.tsx';
import { useLocation } from 'react-router-dom';
import DandelionPage from './pages/Event/DandelionPage.tsx';
import MainPage from './pages/MainPage.tsx';
import ManageAccountsPage from './pages/ManageAccountsPage.tsx';

function App() {
  const location = useLocation();

  // MenuBar를 숨길 경로들
  const hideMenuBarPaths = ['/', '/login', '/signup'];

  // 현재 경로가 해당 배열에 포함되는지 확인
  const shouldShowMenuBar = !hideMenuBarPaths.includes(location.pathname);

  return (
    <div className='min-h-screen bg-background'>
      <div className='flex flex-col min-h-screen'>
        <main>
          <Routes>
            <Route path='/' element={<Onboarding />} />
            <Route path='/main' element={<MainPage />} />
            <Route path='/manage' element={<ManageAccountsPage />} />
            <Route path='/login' element={<Login />} />
            <Route path='/signup' element={<Signup />} />
            <Route path='/organization' element={<Organization />} />
            <Route path='/:userId/donate' element={<AccountDonate />} />
            <Route path='/:userId/donate/:id' element={<AccountDetail />} />
            <Route path='/event'>
              <Route index element={<DandelionPage />} />
              <Route
                element={
                  <TitleLayout title='hihi'>
                    <Outlet />
                  </TitleLayout>
                }
              >
                <Route path='donationking' element={<DonationKingPage />} />
                <Route path='donate' element={<DonatePage />} />
              </Route>
            </Route>
          </Routes>
        </main>
        {/* 조건부 렌더링 */}
        {shouldShowMenuBar && <MenuBar />}
      </div>
    </div>
  );
}

export default App;
