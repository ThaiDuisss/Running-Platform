import axiosClient from "./axiosClient"

export const customPlan = {
    getAllGoal: async () => {
        const data = await axiosClient.get("/goals");
        return data.data.data;
    },
    savePlan: async (payload) => {
        const data = await axiosClient.post("/plan/custom", payload);
        return data.data.data;
    }
}