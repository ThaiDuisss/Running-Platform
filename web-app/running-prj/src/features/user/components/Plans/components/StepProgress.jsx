import React from 'react'

const StepProgress = ({ currentStep = 1, totalSteps = 3, text = "STEP" }) => {
    const percent = (currentStep / totalSteps) * 100;

    return (
        <div style={{ width: "100%" }}>
            {/* Thanh progress */}
            <div
                style={{
                    height: "4px",
                    background: "#e5e7eb",
                    borderRadius: "999px",
                    overflow: "hidden",
                }}
            >
                <div
                    style={{
                        height: "100%",
                        width: `${percent}%`,
                        background: "#2563eb",
                        transition: "width 0.3s ease",
                    }}
                />
            </div>

            {/* Text */}
            <p
                style={{
                    marginTop: "8px",
                    fontSize: "15px",
                    color: "#713edfff",
                    fontWeight: 600,
                }}
            >
                {text} {currentStep} OF {totalSteps}
            </p>
        </div>
    )
}

export default StepProgress
