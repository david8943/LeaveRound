/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        background: 'var(--background)',
        primary: {
          DEFAULT: 'var(--primary)',
          light: 'var(--primary-light)',
        },
        secondary: {
          DEFAULT: 'var(--secondary)',
        },
        complementary: {
          alert: {
            DEFAULT: 'var(--alert)',
          },
        },
      },
      textColor: {
        black: 'var(--text-black)',
        deepgray: 'var(--text-deepgray)',
        disabled: 'var(--text-disabled)',
      },
      fontSize: {
        heading: 'var(--text-heading)',
        body: 'var(--text-body)',
        detail: 'var(--text-detail)',
      },
      borderRadius: {
        xl: 'calc(var(--radius) + 0.75rem)',
        lg: 'var(--radius)',
        md: 'calc(var(--radius) - 0.25rem)',
        sm: 'calc(var(--radius) - 0.5rem)',
      },
      backgroundColor: {
        background: 'var(--background)',
        deepgray: 'var(--background-deepgray)',
        modal: 'var(--modal-background)',
      },
      width: {
        base: 'var(--width-base)',
      },
    },
    fontFamily: {
      body: ['NoonnuBasicGothic', 'ui-sans-serif', 'system-ui', 'sans-serif'],
      heading: ['BobaesumJindo', 'serif'],
    },
  },
  plugins: [require('tailwind-scrollbar-hide'), require('@tailwindcss/line-clamp'),],
};
