import React from "react";

const InstanceWorkoutDay = (props) => {
    const { date, workouts, onAdd, onDelete } = props;

    const dayName = date.toLocaleDateString("en-US", {
        weekday: "short",
    });

    const dayNumber = date.getDate();

    const dateId = date.toISOString().split("T")[0];

    return (
        <div
            style={{
                backgroundColor: "#f1e8e8ff",
                borderBottom: "1px solid black",
                padding: "12px",
                display: "flex",
                gap: "20px",
            }}
        >
            {/* DATE */}
            <div style={{ width: "60px", textAlign: "center" }}>
                <div>{dayName}</div>
                <div style={{ fontWeight: "bold" }}>{dayNumber}</div>
            </div>

            {/* WORKOUT */}
            <div style={{ flex: 1 }}>
                {workouts.length > 0 ? (
                    workouts.map((w) => (
                        <div
                            key={w.id}
                            style={{
                                background: "white",
                                padding: "8px",
                                marginBottom: "6px",
                                borderRadius: "8px",
                            }}
                        >
                            <div style={{ fontWeight: "bold" }}>
                                {w.title}
                            </div>

                            <div style={{ fontSize: "12px" }}>
                                {w.targetDistance} KM - {w.duration} min
                            </div>

                            <button
                                onClick={() => onDelete(w.id)}
                                style={{
                                    marginTop: "4px",
                                    color: "red",
                                    fontSize: "12px",
                                }}
                            >
                                Delete
                            </button>
                        </div>
                    ))
                ) : (
                    <button onClick={() => onAdd(dateId)}>
                        + Add Workout
                    </button>
                )}
            </div>
        </div>
    );
};

export default InstanceWorkoutDay;