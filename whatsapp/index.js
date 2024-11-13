const qrcode = require('qrcode-terminal');
const { Client, LocalAuth } = require('whatsapp-web.js');
const express = require('express');
const bodyParser = require('body-parser');

const client = new Client({
    authStrategy: new LocalAuth({ clientId: "session" })
});

const app = express();
app.use(bodyParser.json());
const port = 5000;

let messageQueue = [];
let isSending = false;

function formatPhoneNumber(phone) {
    let cleaned = phone.replace(/\D/g, '');

    if (!cleaned.startsWith('55')) {
        cleaned = '55' + cleaned;
    }

    if (cleaned.length !== 13 && cleaned.length !== 12) {
        throw new Error('Invalid phone number length');
    }

    return cleaned + '@c.us';
}

function getRandomInterval() {
    // return Math.floor(Math.random() * (60000 - 40000 + 1)) + 40000;
    return 1;
}

async function validateWhatsAppNumber(numberId) {
    try {
        const id = await client.getNumberId(numberId);
        return id !== null;
    } catch (error) {
        console.error('Error validating WhatsApp number:', error);
        return false;
    }
}

async function processMessageQueue() {
    if (isSending || messageQueue.length === 0) return;

    isSending = true;
    const { numeroCompleto, mensagem } = messageQueue[0];

    try {
        const isValid = await validateWhatsAppNumber(numeroCompleto);

        if (isValid) {
            const numberId = await client.getNumberId(numeroCompleto);
            await client.sendMessage(numberId._serialized, mensagem);
            console.log(`Message sent successfully to ${numeroCompleto}`);
            messageQueue.shift();
        } else {
            console.error(`Invalid WhatsApp number: ${numeroCompleto}`);
            messageQueue.shift();
        }
    } catch (err) {
        console.error(`Error sending message to ${numeroCompleto}:`, err);
        if (err.message.includes('invalid') || err.message.includes('not registered')) {
            messageQueue.shift();
        }
    } finally {
        setTimeout(() => {
            isSending = false;
            processMessageQueue();
        }, getRandomInterval());
    }
}

client.on('qr', (qr) => {
    qrcode.generate(qr, { small: true });
    console.log('QR Code generated. Please scan with WhatsApp to authenticate.');
});

client.on('ready', () => {
    console.log('WhatsApp client is ready!');
});

client.on('auth_failure', (err) => {
    console.error('Authentication failed:', err);
});

app.post('/sendmessage', async (req, res) => {
    const { phone, message } = req.body;

    if (!phone || !message) {
        return res.status(400).json({
            status: 'error',
            message: 'Both phone number and message are required'
        });
    }

    try {
        const numeroCompleto = formatPhoneNumber(phone);
        messageQueue.push({ numeroCompleto, mensagem: message });
        processMessageQueue();

        res.status(200).json({
            status: 'success',
            message: 'Message added to queue',
            queuePosition: messageQueue.length
        });
    } catch (error) {
        res.status(400).json({
            status: 'error',
            message: error.message
        });
    }
});

app.get('/health', (req, res) => {
    res.status(200).json({
        status: 'ok',
        queueLength: messageQueue.length,
        isProcessing: isSending
    });
});

client.initialize();

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});