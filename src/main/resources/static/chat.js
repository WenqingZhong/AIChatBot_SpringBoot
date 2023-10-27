function sendMessage() {
    const userInput = document.getElementById('user-input').value;
    const chatbox = document.getElementById('chatbox');

    // Append the user message to the chatbox
    const userDiv = document.createElement('div');
    userDiv.className = 'message user-message';
    userDiv.innerText = userInput;
    chatbox.appendChild(userDiv);

    // Send the user message to the server
    fetch('/api/ask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userInput)
    })
        .then(response => response.json())
        .then(data => {
            // Append the bot response to the chatbox
            const botDiv = document.createElement('div');
            botDiv.className = 'message bot-message';
            botDiv.innerText = data.response;
            chatbox.appendChild(botDiv);
            chatbox.scrollTop = chatbox.scrollHeight; // Scroll to the bottom to see the latest message
        });

    // Clear the input field after sending
    document.getElementById('user-input').value = '';
}
