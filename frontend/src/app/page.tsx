"use client";

import { useEffect, useState } from "react";

type Task = {
  id: number;
  title: string;
  description: string;
  completed: boolean;
};

export default function Home() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [hoveredTask, setHoveredTask] = useState<number | null>(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    // Check if the user is already logged in
    const storedLogin = localStorage.getItem("isLoggedIn");
    if (storedLogin === "true") {
      setIsLoggedIn(true);
      fetchTasks();
    }
  }, []);

  const fetchTasks = () => {
    fetch("http://localhost:8080/api/tasks")
      .then((response) => response.json())
      .then((data) => setTasks(data))
      .catch((error) => console.error("Error fetching tasks:", error));
  };

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (username === "admin" && password === "admin") {
      setIsLoggedIn(true);
      localStorage.setItem("isLoggedIn", "true");
      fetchTasks();
    } else {
      alert("Invalid credentials!");
    }
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    localStorage.removeItem("isLoggedIn");
  };

  const handleAddTask = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !description.trim()) {
      alert("Title and Description are required.");
      return;
    }

    const newTask = { title, description, completed: false };

    try {
      const response = await fetch("http://localhost:8080/api/tasks", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newTask),
      });

      if (!response.ok) throw new Error("Failed to add task");

      const addedTask = await response.json();
      setTasks([...tasks, addedTask]);
      setTitle("");
      setDescription("");
    } catch (error) {
      console.error("Error adding task:", error);
    }
  };

  const markAsCompleted = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/tasks/${id}/complete`, { method: "PUT" });
      if (!response.ok) throw new Error("Failed to update task");

      const updatedTask = await response.json();
      setTasks(tasks.map((task) => (task.id === id ? updatedTask : task)));
    } catch (error) {
      console.error("Error marking task as completed:", error);
    }
  };

  const deleteTask = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/tasks/${id}`, { method: "DELETE" });
      if (!response.ok) throw new Error("Failed to delete task");

      setTasks(tasks.filter((task) => task.id !== id));
    } catch (error) {
      console.error("Error deleting task:", error);
    }
  };

  return (
    <main className="p-6 max-w-lg mx-auto">
      {!isLoggedIn ? (
        <>
          <h1 className="text-2xl font-bold mb-4">Login</h1>
          <form onSubmit={handleLogin} className="mb-6 border p-4 rounded-lg shadow">
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full p-2 mb-2 border rounded"
              required
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2 mb-2 border rounded"
              required
            />
            <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded w-full">
              Login
            </button>
          </form>
        </>
      ) : (
        <>
          <div className="flex justify-between items-center mb-4">
            <h1 className="text-2xl font-bold">To-Do List</h1>
            <button onClick={handleLogout} className="bg-red-500 text-white px-4 py-2 rounded">
              Logout
            </button>
          </div>

          {/* Task Form */}
          <form onSubmit={handleAddTask} className="mb-6 border p-4 rounded-lg shadow">
            <input
              type="text"
              placeholder="Task title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full p-2 mb-2 border rounded"
              required
            />
            <input
              type="text"
              placeholder="Task description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full p-2 mb-2 border rounded"
              required
            />
            <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
              Add Task
            </button>
          </form>

          {/* Task List */}
          <ul>
            {tasks.length > 0 ? (
              tasks.map((task) => (
                <li
                  key={task.id}
                  className="p-2 border-b flex justify-between items-center min-h-[40px] transition-all"
                  onMouseEnter={() => setHoveredTask(task.id)}
                  onMouseLeave={() => setHoveredTask(null)}
                >
                  <div>
                    <strong>{task.title}</strong>: {task.description}{" "}
                    {task.completed ? "✅" : "🟨❓"}
                  </div>
                  <div className="flex gap-2">
                    <button
                      onClick={() => markAsCompleted(task.id)}
                      className={`text-green-500 text-lg transition-opacity ${
                        task.completed || hoveredTask === task.id ? "opacity-100" : "opacity-0"
                      }`}
                      title="Mark as Done"
                    >
                      ✅
                    </button>
                    <button
                      onClick={() => deleteTask(task.id)}
                      className={`text-red-500 text-lg transition-opacity ${
                        hoveredTask === task.id ? "opacity-100" : "opacity-0"
                      }`}
                      title="Delete"
                    >
                      ❌
                    </button>
                  </div>
                </li>
              ))
            ) : (
              <p>No tasks found.</p>
            )}
          </ul>
        </>
      )}
    </main>
  );
}
