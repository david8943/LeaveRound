import { MenuBar } from '@/components/common/MenuBar';
import { Route, Routes, Outlet } from 'react-router-dom';
import { AccountDonate } from './pages/AccountDonatePage.tsx';
import { AccountDetail } from './pages/AccountDetailPage.tsx';
import { HomePage } from './pages/HomePage.tsx';
import DonationKingPage from './pages/Event/DonationKingPage.tsx';
import DonatePage from './pages/Event/DonatePage.tsx';
import TitleLayout from './components/layout/TitleLayout.tsx';

function App() {
  return (
    <div className='min-h-screen bg-background'>
      <div className='flex flex-col min-h-screen'>
        <main className=''>
          <Routes>
            <Route path='/' element={<HomePage />} />
            <Route path='/:userId/donate' element={<AccountDonate />} />
            <Route path='/:userId/donate/:id' element={<AccountDetail />} />
            <Route path='/event'>
              <Route index element={<div>hi</div>} />
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
        <MenuBar />
      </div>
    </div>
  );
}

export default App;
