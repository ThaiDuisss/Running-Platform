export default function ValidationItem({ label, current, target }) {
    return (
        <div>
            <small>{label}</small>
            <div>
                {current} / {target}
            </div>
        </div>
    );
}