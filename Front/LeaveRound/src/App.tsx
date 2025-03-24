import TitleLayout from './components/layout/TitleLayout';
import DonatePage from './pages/Event/DonatePage';

function App() {
  return (
    <div>
      <TitleLayout title='기부하기'>
        <DonatePage />
      </TitleLayout>
    </div>
  );
}

export default App;
