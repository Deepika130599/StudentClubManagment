import { configureStore } from '@reduxjs/toolkit';
import authReducer from './auth/authSlice'; // Import authentication slice

// Configure the Redux store
const store = configureStore({
  reducer: {
    auth: authReducer, // Add auth reducer to the store
  },
});

export default store;
