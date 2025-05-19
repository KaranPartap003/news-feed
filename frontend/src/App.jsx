import './custom.scss'
import NewsFeed from './Components/NewsFeed'
import Login from './Components/Login'
import SignUp from './Components/SignUp'
import { BrowserRouter, Routes , Route } from 'react-router-dom'

function App() {
  return (
    <BrowserRouter>
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/app" element={
        <>
        <h1 style={{color:'#1DCD9F'}}>Welcome to the News Feed</h1>
        <NewsFeed />
        </>
      } />
      <Route path='/signup' element={<SignUp />} />
    </Routes>
    </BrowserRouter>
  )
}

export default App


