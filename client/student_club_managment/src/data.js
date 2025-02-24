import config from './config'


const fetchAPI = async(URL, method = 'GET', data = null) => {
    try {
        const options = {
            method,
            headers: {
                'Content-Type' : 'application/json'
            }
        };
        if(data) {
            options.body = JSON.stringify(data);
        }

        const response = await fetch(`${config.apiBaseUrl}${URL}`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();

    }
    catch(error){
        console.error(error)
        return {error: error.message};
    }
}

// User API

export const createUser = (data) => fetchAPI('/users', 'POST', data);
export const userAuth = (data) => fetchAPI('/user/auth/login', 'POST', data ); 
export const getUserClubs = (id) => fetchAPI(`/user/${id}`, 'GET');
export const fetchUserData = (email) => fetchAPI(`/user/email?email=${email}`, 'GET');

// Clubs API
export const fetchAllClubs = () => fetchAPI(`/clubs`, 'GET');
export const joinClub = (data) => fetchAPI('/club-requests', 'POST', data ); 

// Announcments
export const fetchAnnouncements = (clubId) => fetchAPI(`/announcements/${clubId}`, 'GET')