export default function FuturisticAuthHomepage() {
  const features = [
    {
      title: 'Secure Authentication',
      description:
        'Enterprise-grade authentication with JWT, OAuth, biometric support, and multi-factor security.',
      icon: '🔐',
    },
    {
      title: 'Lightning Fast',
      description:
        'Optimized performance with instant login flows and real-time session management.',
      icon: '⚡',
    },
    {
      title: 'Dark & Light Mode',
      description:
        'Beautiful adaptive UI crafted for both dark and light environments seamlessly.',
      icon: '🌗',
    },
    {
      title: 'Developer Friendly',
      description:
        'Easy SDK integration, API-first architecture, and comprehensive documentation.',
      icon: '🧠',
    },
  ]

  const stats = [
    {
      label: 'Active Users',
      value: '2M+',
    },
    {
      label: 'Authentication Requests',
      value: '500M+',
    },
    {
      label: 'Uptime',
      value: '99.99%',
    },
  ]

  return (
    <div className="min-h-screen overflow-hidden bg-white text-black transition-colors duration-500 dark:bg-black dark:text-white">
      {/* Background Effects */}
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute left-0 top-0 h-[500px] w-[500px] rounded-full bg-cyan-500/20 blur-3xl dark:bg-cyan-500/10" />
        <div className="absolute bottom-0 right-0 h-[400px] w-[400px] rounded-full bg-violet-500/20 blur-3xl dark:bg-violet-500/10" />
        <div className="absolute left-1/2 top-1/2 h-[300px] w-[300px] -translate-x-1/2 -translate-y-1/2 rounded-full bg-blue-500/10 blur-3xl dark:bg-blue-500/5" />
      </div>

      {/* Hero Section */}
      <section className="relative mx-auto flex max-w-7xl flex-col items-center px-6 pb-24 pt-24 text-center lg:pt-32">
        <div className="mb-6 rounded-full border border-cyan-500/20 bg-cyan-500/10 px-5 py-2 text-sm text-cyan-700 backdrop-blur-md dark:text-cyan-300">
          Next Generation Authentication Platform
        </div>

        <h1 className="max-w-5xl bg-gradient-to-b from-black via-black/90 to-black/60 bg-clip-text text-5xl font-black leading-tight tracking-tight text-transparent dark:from-white dark:via-white dark:to-white/50 sm:text-6xl lg:text-7xl">
          Secure Authentication
          <br />
          For The Future Web
        </h1>

        <p className="mt-8 max-w-2xl text-lg leading-relaxed text-black/60 dark:text-white/60">
          Build powerful authentication experiences with advanced security,
          AI-powered fraud protection, passwordless login, and stunning user
          interfaces.
        </p>

        <div className="mt-10 flex flex-col items-center gap-4 sm:flex-row">
          <button className="rounded-2xl bg-gradient-to-r from-cyan-500 to-violet-500 px-8 py-4 text-base font-semibold text-white shadow-2xl shadow-cyan-500/30 transition hover:scale-105">
            Launch Dashboard
          </button>

          <button className="rounded-2xl border border-black/10 bg-white/50 px-8 py-4 text-base font-semibold backdrop-blur-xl transition hover:bg-black/5 dark:border-white/10 dark:bg-white/5 dark:hover:bg-white/10">
            View Documentation
          </button>
        </div>

        {/* Hero Card */}
        <div className="relative mt-20 w-full max-w-5xl rounded-[32px] border border-black/10 bg-white/70 p-8 shadow-2xl shadow-cyan-500/10 backdrop-blur-2xl dark:border-white/10 dark:bg-white/5">
          <div className="grid gap-6 lg:grid-cols-3">
            {stats.map((stat) => (
              <div
                key={stat.label}
                className="rounded-2xl border border-black/10 bg-black/[0.02] p-6 dark:border-white/10 dark:bg-white/[0.03]"
              >
                <div className="text-4xl font-black text-cyan-500">
                  {stat.value}
                </div>
                <div className="mt-2 text-sm text-black/60 dark:text-white/60">
                  {stat.label}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features */}
      <section
        id="features"
        className="mx-auto max-w-7xl px-6 py-24"
      >
        <div className="mb-16 text-center">
          <div className="mb-4 text-sm font-semibold uppercase tracking-[0.3em] text-cyan-500">
            Features
          </div>
          <h2 className="text-4xl font-black sm:text-5xl">
            Everything You Need
          </h2>
          <p className="mx-auto mt-6 max-w-2xl text-lg text-black/60 dark:text-white/60">
            A complete authentication ecosystem designed for modern applications.
          </p>
        </div>

        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          {features.map((feature) => (
            <div
              key={feature.title}
              className="group rounded-[28px] border border-black/10 bg-white/60 p-8 backdrop-blur-xl transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl hover:shadow-cyan-500/10 dark:border-white/10 dark:bg-white/5"
            >
              <div className="mb-6 flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-500/20 to-violet-500/20 text-3xl backdrop-blur-md">
                {feature.icon}
              </div>

              <h3 className="text-2xl font-bold">{feature.title}</h3>

              <p className="mt-4 leading-relaxed text-black/60 dark:text-white/60">
                {feature.description}
              </p>
            </div>
          ))}
        </div>
      </section>

      {/* Security Section */}
      <section
        id="security"
        className="mx-auto max-w-7xl px-6 py-24"
      >
        <div className="grid items-center gap-16 lg:grid-cols-2">
          <div>
            <div className="mb-4 text-sm font-semibold uppercase tracking-[0.3em] text-violet-500">
              Advanced Security
            </div>

            <h2 className="text-4xl font-black leading-tight sm:text-5xl">
              Built With Modern Protection Layers
            </h2>

            <p className="mt-6 text-lg leading-relaxed text-black/60 dark:text-white/60">
              Protect user identities using multi-factor authentication,
              AI-powered anomaly detection, device verification, and end-to-end
              encrypted sessions.
            </p>

            <div className="mt-10 space-y-4">
              {[
                'Multi-factor authentication',
                'Biometric login support',
                'AI fraud detection',
                'End-to-end encrypted sessions',
              ].map((item) => (
                <div
                  key={item}
                  className="flex items-center gap-4 rounded-2xl border border-black/10 bg-white/50 p-4 backdrop-blur-xl dark:border-white/10 dark:bg-white/5"
                >
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-r from-cyan-500 to-violet-500 text-sm font-bold text-white">
                    ✓
                  </div>
                  <span className="font-medium">{item}</span>
                </div>
              ))}
            </div>
          </div>

          <div className="relative">
            <div className="rounded-[32px] border border-black/10 bg-white/60 p-8 shadow-2xl shadow-violet-500/10 backdrop-blur-2xl dark:border-white/10 dark:bg-white/5">
              <div className="space-y-6">
                <div className="flex items-center justify-between rounded-2xl border border-black/10 bg-black/[0.02] p-5 dark:border-white/10 dark:bg-white/[0.03]">
                  <div>
                    <div className="text-sm text-black/50 dark:text-white/50">
                      Authentication Status
                    </div>
                    <div className="mt-2 text-2xl font-bold">
                      Verified Secure
                    </div>
                  </div>
                  <div className="h-4 w-4 rounded-full bg-green-500 shadow-lg shadow-green-500/50" />
                </div>

                <div className="rounded-2xl border border-black/10 bg-gradient-to-br from-cyan-500/10 to-violet-500/10 p-6 dark:border-white/10">
                  <div className="text-sm text-black/50 dark:text-white/50">
                    Active Sessions
                  </div>
                  <div className="mt-3 text-5xl font-black">12,483</div>
                  <div className="mt-2 text-sm text-green-500">
                    +14% this week
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="rounded-2xl border border-black/10 bg-black/[0.02] p-5 dark:border-white/10 dark:bg-white/[0.03]">
                    <div className="text-sm text-black/50 dark:text-white/50">
                      API Response
                    </div>
                    <div className="mt-3 text-3xl font-bold">42ms</div>
                  </div>

                  <div className="rounded-2xl border border-black/10 bg-black/[0.02] p-5 dark:border-white/10 dark:bg-white/[0.03]">
                    <div className="text-sm text-black/50 dark:text-white/50">
                      Success Rate
                    </div>
                    <div className="mt-3 text-3xl font-bold">99.9%</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="mx-auto max-w-7xl px-6 py-24">
        <div className="relative overflow-hidden rounded-[40px] border border-black/10 bg-gradient-to-br from-cyan-500 to-violet-600 p-12 text-center text-white shadow-2xl shadow-cyan-500/20 dark:border-white/10">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_right,rgba(255,255,255,0.2),transparent_40%)]" />

          <div className="relative z-10">
            <h2 className="text-4xl font-black sm:text-5xl">
              Ready To Secure Your Platform?
            </h2>

            <p className="mx-auto mt-6 max-w-2xl text-lg text-white/80">
              Start building secure and scalable authentication experiences in
              minutes.
            </p>

            <div className="mt-10 flex flex-col items-center justify-center gap-4 sm:flex-row">
              <button className="rounded-2xl bg-white px-8 py-4 font-semibold text-black transition hover:scale-105">
                Start Free Trial
              </button>

              <button className="rounded-2xl border border-white/20 bg-white/10 px-8 py-4 font-semibold backdrop-blur-xl transition hover:bg-white/20">
                Book Demo
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer
        id="contact"
        className="border-t border-black/10 py-10 dark:border-white/10"
      >
        <div className="mx-auto flex max-w-7xl flex-col items-center justify-between gap-6 px-6 text-center md:flex-row md:text-left">
          <div>
            <div className="text-2xl font-bold">AuthNova</div>
            <div className="mt-2 text-sm text-black/50 dark:text-white/50">
              Futuristic authentication infrastructure for modern applications.
            </div>
          </div>

          <div className="flex items-center gap-6 text-sm text-black/50 dark:text-white/50">
            <a href="#">Privacy</a>
            <a href="#">Terms</a>
            <a href="#">Support</a>
          </div>
        </div>
      </footer>
    </div>
  )
}
