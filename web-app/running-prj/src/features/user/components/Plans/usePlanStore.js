import {
    DAYS
} from "@/shared/constant/constants";
import {
    create
} from "zustand";
import {
    persist
} from "zustand/middleware";

export const usePlanStore = create(
    persist(
        (set, get) => ({
            // 🔥 state
            form: {
                title: "",
                goal: 0,
                durationWeeks: 4,
                daysPerWeek: 0,
                selectedDays: [],
                selectedLevel: "",
                params: {},
                dayStart: new Date(),
                schedule: []
            },

            applyWeekTemplate: (sourceWeekIndex, targetWeeks) =>
                set((state) => {
                    const sourceWeek = state.form.schedule[sourceWeekIndex];

                    return {
                        form: {
                            ...state.form,
                            schedule: state.form.schedule.map((week) => {
                                if (targetWeeks.includes(week.week)) {
                                    return {
                                        ...week,
                                        days: sourceWeek.days.map((d) => {
                                            const targetDay = week.days.find(x => x.dayName === d.dayName);

                                            return {
                                                ...targetDay,
                                                type: d.type,
                                                targetDistance: d.targetDistance,
                                                duration: d.duration
                                            };
                                        })
                                    };
                                }
                                return week;
                            })
                        }
                    };
                }),

            getTrainingDaysPayload: () => {
                const {
                    form
                } = get();

                return form.schedule
                    .flatMap(week => week.days)
                    .filter(day => day.type !== "REST" && day.type !== "NONE")
                    .map(day => ({
                        date: day.date,
                        type: day.type,
                        targetDistance: day.targetDistance,
                        duration: day.duration
                    }));
            },

            generateSchedule: () => {
                const {
                    form
                } = get();

                const weeks = [];
                const startDate = new Date(form.dayStart);
                const totalWeeks = form.durationWeeks;

                for (let w = 0; w < totalWeeks; w++) {
                    const week = {
                        week: w + 1,
                        days: []
                    };

                    for (let d = 0; d < 7; d++) {
                        const current = new Date(startDate);
                        current.setDate(startDate.getDate() + w * 7 + d);

                        const dayIndex = current.getDay();
                        const dayName = DAYS[dayIndex];

                        const isPast =
                            w === 0 &&
                            current.toDateString() !== startDate.toDateString() &&
                            current < startDate;

                        const isPreferred = form.selectedDays.includes(dayName);

                        let type = "REST";

                        if (isPast) {
                            type = "NONE";
                        } else if (isPreferred) {
                            if (d % 2 === 0) {
                                type = "RUN";
                            } else {
                                type = "Long";

                            }
                        }

                        week.days.push({
                            date: current.toISOString().split("T")[0],
                            dayName,
                            type,
                            targetDistance: type === "RUN" ? 5 : 0,
                            duration: type === "RUN" ? 30 : 0
                        });
                    }

                    weeks.push(week);
                }

                set((state) => ({
                    form: {
                        ...state.form,
                        schedule: weeks
                    }
                }));
            },

            setForm: (data) => set({
                form: data
            }),

            updateForm: (field, value) =>
                set((state) => ({
                    form: {
                        ...state.form,
                        [field]: value
                    }
                })),

            updateParams: (name, value) =>
                set((state) => ({
                    form: {
                        ...state.form,
                        params: {
                            ...state.form.params,
                            [name]: value
                        }
                    }
                })),

            resetForm: () =>
                set({
                    form: {
                        title: "",
                        goal: 0,
                        durationWeeks: 4,
                        daysPerWeek: 0,
                        selectedDays: [],
                        selectedLevel: "",
                        params: {},
                        dayStart: new Date(),
                        schedule: []
                    }
                }),

            updateDay: (updatedDay) =>
                set((state) => ({
                    form: {
                        ...state.form,
                        schedule: state.form.schedule.map((week) => ({
                            ...week,
                            days: week.days.map((d) =>
                                d.date === updatedDay.date ? updatedDay : d
                            )
                        }))
                    }
                })),
        }), {
            name: "plan-storage", // 🔥 key localStorage


            onRehydrateStorage: () => (state) => {
                if (state.form.dayStart) {
                    state.form.dayStart = new Date(state.form.dayStart);
                }
            }
        }
    )
);