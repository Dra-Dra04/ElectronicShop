/**
 * Payment Callback Handler
 * Xử lý callback thanh toán từ mock payment gateway
 */

const PAYMENT_SERVICE = "http://localhost:8086/api/payments";
const ORDER_SERVICE = "http://localhost:8087/api/orders";

/**
 * Gửi callback thanh toán thành công
 * @param {number} paymentId - ID của payment
 */
async function sendPaymentSuccess(paymentId) {
    try {
        console.log("Sending payment success callback for paymentId:", paymentId);
        
        const response = await fetch(`${PAYMENT_SERVICE}/${paymentId}/success`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to send success callback: ${response.statusText}`);
        }

        const data = await response.json();
        console.log("Payment success response:", data);
        
        // Nếu orderId được trả về, có thể cập nhật order status
        if (data.orderId) {
            console.log("Order ID:", data.orderId, "Status:", data.status);
        }
        
        return data;
    } catch (error) {
        console.error("Error sending payment success callback:", error);
        throw error;
    }
}

/**
 * Gửi callback thanh toán thất bại
 * @param {number} paymentId - ID của payment
 */
async function sendPaymentFailed(paymentId) {
    try {
        console.log("Sending payment failed callback for paymentId:", paymentId);
        
        const response = await fetch(`${PAYMENT_SERVICE}/${paymentId}/fail`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to send failed callback: ${response.statusText}`);
        }

        const data = await response.json();
        console.log("Payment failed response:", data);
        
        return data;
    } catch (error) {
        console.error("Error sending payment failed callback:", error);
        throw error;
    }
}

/**
 * Gửi generic payment callback (dùng cho frontend mock)
 * @param {number} paymentId - ID của payment
 * @param {string} status - SUCCESS hoặc FAILED
 */
async function sendPaymentCallback(paymentId, status) {
    try {
        console.log(`Sending payment callback: paymentId=${paymentId}, status=${status}`);
        
        const response = await fetch(`${PAYMENT_SERVICE}/callback`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                paymentId: paymentId,
                status: status,
                transactionId: `TXN_${Date.now()}`,
                message: status === 'SUCCESS' ? 'Payment completed' : 'Payment failed'
            })
        });

        if (!response.ok) {
            throw new Error(`Failed to send callback: ${response.statusText}`);
        }

        const data = await response.json();
        console.log("Payment callback response:", data);
        
        return data;
    } catch (error) {
        console.error("Error sending payment callback:", error);
        throw error;
    }
}

/**
 * Giả lập thanh toán thành công sau một khoảng thời gian
 * @param {number} paymentId - ID của payment
 * @param {number} delayMs - Độ trễ tính bằng milliseconds (default: 3000)
 */
async function simulateSuccessfulPayment(paymentId, delayMs = 3000) {
    return new Promise((resolve) => {
        setTimeout(async () => {
            try {
                const result = await sendPaymentCallback(paymentId, 'SUCCESS');
                resolve(result);
            } catch (error) {
                console.error("Error in simulated payment:", error);
                resolve(null);
            }
        }, delayMs);
    });
}

/**
 * Giả lập thanh toán thất bại sau một khoảng thời gian
 * @param {number} paymentId - ID của payment
 * @param {number} delayMs - Độ trễ tính bằng milliseconds (default: 3000)
 */
async function simulateFailedPayment(paymentId, delayMs = 3000) {
    return new Promise((resolve) => {
        setTimeout(async () => {
            try {
                const result = await sendPaymentCallback(paymentId, 'FAILED');
                resolve(result);
            } catch (error) {
                console.error("Error in simulated payment:", error);
                resolve(null);
            }
        }, delayMs);
    });
}

// Export cho browser
if (typeof module === 'undefined') {
    window.PaymentCallback = {
        sendPaymentSuccess,
        sendPaymentFailed,
        sendPaymentCallback,
        simulateSuccessfulPayment,
        simulateFailedPayment
    };
}
