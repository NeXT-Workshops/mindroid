% tudposter is the poster-class as described in the TUD-Design-Manual
% It is based on (makes heavy use of) the KOMA-Class scrartcl
% This class is provided "as is", the authors take no responsibility
% for any effects the style file has on the user, the user's life, family,
% pets, scientific or other career etc.
% 
% When making changes to this style file or the accompanying package files
% please notify the authors:
% tuddesign@pro-kevin.de
% Good luck!

\def\fileversion{1.9}
\def\filedate{2009/09/09}
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{tuddesign/tudexercise}
 [\filedate\space\fileversion\space 
   Clemens von Loewenich's and Joe Werner's TUD-Design package]

\newif\ifTUD@longdoc \TUD@longdocfalse
\newif\ifTUD@chapname \TUD@chapnametrue
\newcommand{\TUD@sectionname@en}{Problem}
\newcommand{\TUD@sectionname@de}{Aufgabe}

\RequirePackage{xkeyval}
\RequirePackage{ifthen}

% Verarbeitung der Optionen
  \DeclareOptionX<\@currname>{nochapname}{%
    \TUD@chapnamefalse}

  \DeclareOptionX<\@currname>{accentcolor}{%
    \PassOptionsToPackage{accentcolor=#1}{tuddesign/colours/tudcolours}}

  \DeclareOptionX<\@currname>{colortitle}{%
    \PassOptionsToPackage{colortitle}{tuddesign/poster/tudpostr_title}}

  \DeclareOptionX<\@currname>{colorback}{%
    \ClassWarning{\@currname}{Option '\CurrentOption` not supported!}}

  \DeclareOptionX<\@currname>{colorbacktitle}{%
    \PassOptionsToPackage{colorbacktitle}{tuddesign/poster/tudpostr_title}
    \PassOptionsToPackage{colorbacksubtitle}{tuddesign/poster/tudpostr_title}}

  \DeclareOptionX<\@currname>{inverttitle}{%
    \PassOptionsToPackage{inverttitle}{tuddesign/poster/tudpostr_title}}

  \DeclareOptionX<\@currname>{inverttitlerule}{%
    \ClassWarning{\@currname}{Option '\CurrentOption` not supported!}}

  \DeclareOptionX<\@currname>{blackrule}{%
    \PassOptionsToPackage{blackrule}{tuddesign/base/tudpage}}

  \DeclareOptionX<\@currname>{longdoc}{%
    \ClassWarning{\@currname}{Option '\CurrentOption` not supported!}}

  \DeclareOptionX<\@currname>{pagingbar}{%
    \PassOptionsToPackage{pagingbar}{tuddesign/base/tudpage}}

  \DeclareOptionX<\@currname>{nopartpage}{%
    \PassOptionsToPackage{nopartpage}{tuddesign/base/tudheading}%
    \PassOptionsToPackage{nopartpage}{tuddesign/poster/tudpostr_title}}

  \DeclareOptionX<\@currname>{nochapterpage}{%
    \ClassWarning{\@currname}{Option '\CurrentOption` not supported!}}

  \DeclareOptionX<\@currname>{bigchapter}{%
    \ClassWarning{\@currname}{Option '\CurrentOption` not supported!}}
  
  \DeclareOptionX<\@currname>{paper}{%
    \ClassWarning{\@currname}{Option `paper=#1' ignored!}}

  \DeclareOptionX<\@currname>{paper}{%
    \ifthenelse{\equal{#1}{a4}\or\equal{#1}{a4report}}{%
      \ClassInfo{\@currname}{Option `paper=a4report' selected.}
      \PassOptionsToPackage{paper=a4report}{tuddesign/base/tudpage}
      \PassOptionsToPackage{a4}{tuddesign/report/tudreprt_fonts}}{%
    \ifthenelse{\equal{#1}{a5}\or\equal{#1}{a5report}}{%
      \ClassInfo{\@currname}{Option `paper=a5report' selected.}
      \PassOptionsToPackage{paper=a5report}{tuddesign/base/tudpage}
      \PassOptionsToPackage{a5}{tuddesign/report/tudreprt_fonts}}{%
    \ClassWarning{\@currname}{Option `paper=#1' ignored!}%
  }}}

  \DeclareOptionX<\@currname>{landscape}{%
      \PassOptionsToPackage{landscape}{tuddesign/base/tudpage}}

  \DeclareOptionX<\@currname>{8pt}{%
    \PassOptionsToPackage{8pt}{tuddesign/report/tudreprt_fonts}}

  \DeclareOptionX<\@currname>{9.5pt}{%
    \PassOptionsToPackage{9.5pt}{tuddesign/report/tudreprt_fonts}}

  \DeclareOptionX<\@currname>{10pt}{%
    \PassOptionsToPackage{10pt}{tuddesign/report/tudreprt_fonts}}

  \DeclareOptionX<\@currname>{11pt}{%
    \PassOptionsToPackage{11pt}{tuddesign/report/tudreprt_fonts}}

  \DeclareOptionX<\@currname>{12pt}{%
    \PassOptionsToPackage{12pt}{tuddesign/report/tudreprt_fonts}}

  \DeclareOptionX<\@currname>{noresetcounter}{%
    \ClassWarning{\@currname}{Option '\CurrentOption` not supported!}}

  \DeclareOptionX<\@currname>{noheadingspace}{%
    \PassOptionsToPackage{noheadingspace}{tuddesign/base/tudheading}}
 
  \DeclareOptionX<\@currname>{firstlineindent}{%
    \PassOptionsToPackage{firstlineindent}{tuddesign/base/tudheading}}

  \DeclareOptionX<\@currname>{numbersubsubsec}{%
    \PassOptionsToPackage{numbersubsubsec}{tuddesign/base/tudheading}}

  \DeclareOptionX<\@currname>{marginparwidth}{%
    \AtEndOfClass{\TUD@setmarginpar{#1}}}

  \DeclareOptionX<\@currname>{solution}{%
    \renewcommand{\TUD@sectionname@en}{Solution}%
    \renewcommand{\TUD@sectionname@de}{L\char246sung}}

  \DeclareOptionX*{%
    \ClassInfo{\@currname}{Unknown option '\CurrentOption`. Pass to scrartcl class.}
    \PassOptionsToClass{\CurrentOption}{scrartcl}}

\ExecuteOptionsX<\@currname>{paper=a4report}
\ProcessOptionsX<\@currname>
\let\@classoptionslist\relax

% Option article always enabled
\PassOptionsToPackage{nopartpage}{tuddesign/poster/tudposter_title}
\PassOptionsToPackage{nopartpage}{tuddesign/base/tudheading}
\PassOptionsToPackage{noheadingspace}{tuddesign/base/tudheading}

% Load the KOMA-Class
\PassOptionsToClass{onelinecaption}{scrartcl}
\LoadClass{scrartcl}

% Include "niceties"
\RequirePackage{calc}

% Load the TUD-colourscheme
\RequirePackage{tuddesign/colours/tudcolours}

% Waehle die TUD-Standardschriftarten
\RequirePackage{tuddesign/poster/tudpostr_fonts}
\RequirePackage{tuddesign/report/tudreprt_fonts}
\renewcommand{\titlesize}{\fontsize{24}{26}\selectfont}

%  Separator zwischen Kopfzeite bzw. Fusszeile und Text
\newskip\TUD@headfootsep
  \TUD@headfootsep \baselineskip

% Load pagestyle (bars, papersize, margins)
\RequirePackage{tuddesign/base/tudpage}

% Load headingstyle for chapter, section etc.
\RequirePackage{tuddesign/base/tudheading}
 
% Load redefinitions
\RequirePackage{tuddesign/base/tudredef}

%make sure we do not break the horizontal raster
\flushbottom

% definiert das Aussehen der Titleseite
\RequirePackage{tuddesign/poster/tudpostr_title}

% Definiert Besondere Befehle und aussehen f"ur Klausuren
\RequirePackage{tuddesign/exercise/tudexrcse_exame}

%%% Unterdrueckt Hurenkinder und Schusterjungen %%%
    \clubpenalty = 10000            % 10000 schlie"st Schusterjungen aus
    \widowpenalty = 10000           % 10000 schlie"st Hurenkinder aus
    \displaywidowpenalty = 10000    % f"ur Formeln
%%% ENDE Unterdueckt Hurenkinder und Schusterjungen %%%

%% modified text-breaking parameters, intended for German language
    % Since German words usually are longer than English ones we allow longer
    % interword spaces as well. A line now may have a badness up to 2500, over
    % 1500 a warning is to be given. Furthermore penalties for hyphenation are
    % lowered because narrow typesetting is preferred against few hyphenations
  \tolerance2500\relax\hbadness1500\relax\doublehyphendemerits50000\relax%  
  \finalhyphendemerits25000\relax\adjdemerits50000\relax%  
%%% END %%% modified text-breaking parameters, intended for German language

\if@twocolumn\setcapindent{0pt}\fi

% Jetzt setze den Seitenstil und andere Dinge
\AtBeginDocument{%
  \pagestyle{plain}%
    \ifTUD@chapname%
      \@ifpackageloaded{babel}{%
        \addto\captionsenglish{\renewcommand\sectionname{\TUD@sectionname@en}}%
        \addto\captionsgerman{\renewcommand\sectionname{\TUD@sectionname@de}}%
        \addto\captionsngerman{\renewcommand\sectionname{\TUD@sectionname@de}}%
      }{}%
      \def\sectionname{\TUD@sectionname@en}%
      \@ifundefined{captionsenglish}{}{%
        \g@addto@macro\captionsenglish{\def\sectionname{\TUD@sectionname@en}}%
        \captionsenglish%
      }%
      \@ifundefined{captionsgerman}{}{%
        \g@addto@macro\captionsgerman{\def\sectionname{\TUD@sectionname@de}}%
        \@ifpackageloaded{german}{\captionsgerman}{}%
      }%
      \@ifundefined{captionsngerman}{}{%
        \g@addto@macro\captionsngerman{\def\sectionname{\TUD@sectionname@de}}%
        \@ifpackageloaded{ngerman}{\captionsngerman}{}%
      }%
      \renewcommand{\thesection}{\sectionname~\arabic{section}}%
    \fi%
}

\renewcommand{\labelenumi}{\alph{enumi})}
\renewcommand{\labelenumii}{\arabic{enumii}.}

\endinput
