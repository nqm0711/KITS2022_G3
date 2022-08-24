import axios from 'axios';

const AUTH_API_BASE_URL = 'http://localhost:8080/api/auth';

class AuthService {
    login(credentials) {
        return axios.post(AUTH_API_BASE_URL + '/login', credentials)
            .then((response) => {
                if (response.data) {
                    localStorage.setItem('user', JSON.stringify(response.data))
                    localStorage.setItem('uid', JSON.stringify(response.data.id))
                }
                return response.data
            })
    }

    logout() {
        return axios.get(AUTH_API_BASE_URL + '/logout');
    }

    register(credentials) {
        return axios.post(AUTH_API_BASE_URL + '/register',credentials)
    }

    getCurrentUser() {
        JSON.parse(localStorage.getItem('user'))
    }
}

export default new AuthService()