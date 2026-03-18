import React from 'react'

const InstanceWorkoutDay = (props) => {
    const { workoutDay } = props;
    const now = Date.now();
    const dateDay = new Date(props.date);
    const dayName = date.toLocaleDateString("en-US", {
        weekday: "short",
    });

    const dayNumber = date.getDate();
    return (
        <div style={{ backgroundColor: "#f1e8e8ff", borderBottom: "1px solid black" }}>
            <div>aa</div>
            <div>aa</div>
        </div>
    )
}

export default InstanceWorkoutDay;
