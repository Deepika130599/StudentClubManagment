import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { userAuth, fetchUserData } from '../../data'; // Import login function from API file

// Async action for login
export const loginUser = createAsyncThunk(
  'auth/loginUser', // Action name
  async ({ email, password }, { rejectWithValue }) => {
    try {
      const response = await userAuth({"email": email, "password": password}); // Call API function
      return { token: response.data, email };
    } catch (error) {
      return rejectWithValue(error.message); // Fail: Return error message
    }
  }
);


// Async action: Fetch user details using email
export const fetchUserDetails = createAsyncThunk(
  'auth/fetchUserDetails',
  async (email, { getState, rejectWithValue }) => {
    try {
      const token = getState().auth.token; // Get token from Redux state
      if (!token) throw new Error('No token available');
      const userData = await fetchUserData(email); // Fetch user details using email
      return userData;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);



// Create Redux slice for authentication
const authSlice = createSlice({
  name: 'auth', // Name of slice
  initialState: {
    user: null, // Stores user data
    token: null, // Stores authentication token
    isLoading: false, // Tracks loading state
    error: null, // Stores error messages
  },
  reducers: {
    logout: (state) => {
      state.user = null;
      state.token = null;
      localStorage.removeItem('token'); // Remove token from storage
    },
  },
  extraReducers: (builder) => {
    builder
      // Handle Login
      .addCase(loginUser.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.isLoading = false;
        state.token = action.payload.token; // Store token
        localStorage.setItem('token', action.payload.token); // Save token
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      })

      // Handle Fetch User Details
      .addCase(fetchUserDetails.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(fetchUserDetails.fulfilled, (state, action) => {
        state.isLoading = false;
        state.user = action.payload; // Store user details in Redux
      })
      .addCase(fetchUserDetails.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
      }); 


  },
});

export const { logout } = authSlice.actions; // Export logout action
export default authSlice.reducer; // Export reducer
