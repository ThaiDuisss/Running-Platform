import axiosClient from "@/shared/services/axiosClient";
import {
    ApiEndpoints
} from "./AppUrlConstant";

const BASE = "/plans";

export const planService = {
    // Get plans by year and month
    getPlansByMonth: async (year, month) => {
        try {
            const response = await axiosClient.get(`${BASE}?year=${year}&month=${month}`);
            return response.data;
        } catch (error) {
            console.error("Get plans by month error:", error);
            throw error;
        }
    },

    // Create a new plan
    createPlan: async (payload) => {
        try {
            const response = await axiosClient.post(BASE, payload);
            return response.data;
        } catch (error) {
            console.error("Create plan error:", error);
            throw error;
        }
    },

    // Update an existing plan
    updatePlan: async (id, payload) => {
        try {
            const response = await axiosClient.put(`${BASE}/${id}`, payload);
            return response.data;
        } catch (error) {
            console.error("Update plan error:", error);
            throw error;
        }
    },

    // Get a plan by ID
    getPlanById: async (id) => {
        try {
            const response = await axiosClient.get(`${BASE}/${id}`);
            return response.data;
        } catch (error) {
            console.error("Get plan by ID error:", error);
            throw error;
        }
    },

    // Delete a plan
    deletePlan: async (id) => {
        try {
            const response = await axiosClient.delete(`${BASE}/${id}`);
            return response.data;
        } catch (error) {
            console.error("Delete plan error:", error);
            throw error;
        }
    }
};