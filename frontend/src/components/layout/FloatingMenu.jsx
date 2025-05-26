import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import DocsButton from "./DocsButton";
import ThemeButton from "./ThemeButton";
import "@/css/FloatingMenu.css";
import { faEllipsisVertical } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const FloatingMenu = () => {
  const [open, setOpen] = useState(false);

  const buttonVariants = {
    hidden: { opacity: 0, y: 10 },
    visible: (i) => ({
      opacity: 1,
      y: 0,
      transition: { delay: i * 0.05, type: "spring", stiffness: 300 }
    }),
    exit: { opacity: 0, y: 10, transition: { duration: 0.1 } }
  };

  const buttons = [
    { component: <DocsButton />, key: "docs", onClick: () => setOpen(false) },
    { component: <ThemeButton />, key: "theme", onClick: () => setOpen(false) }
  ];

  return (
    <div className="floating-menu">
      <AnimatePresence>
        {open && (
          <motion.div
            className="menu-buttons"
            initial="hidden"
            animate="visible"
            exit="hidden"
          >
            {buttons.map((btn, i) => (
              <motion.div
                key={btn.key}
                custom={i}
                variants={buttonVariants}
                initial="hidden"
                animate="visible"
                exit="exit"
                onClick={btn.onClick}
              >
                {btn.component}
              </motion.div>
            ))}
          </motion.div>
        )}
      </AnimatePresence>

      <button className="menu-toggle" onClick={() => setOpen(prev => !prev)}>
        <FontAwesomeIcon icon={faEllipsisVertical} className="fa-lg" />
      </button>
    </div>
  );
};

export default FloatingMenu;