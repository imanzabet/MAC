#include &lt;stdio.h&gt;
/* Maximum problem size, modify to solve bigger problems */
#define MAXM 5     /* max no. of knapsack constraints */
#define MAXN 20    /* max no. of multiple-choice constraints */
#define MAXB 200   /* biggest knapsack capacity */
/* Mnemonic definitions */
#define YES 1
#define NO 0
typedef int bool;
/* Structure of a branch-and-bound tree node */
typedef struct NODE {
int nodeno,          /* node's serial no */
bound,           /* upper bound of the node's partial problem */
ifix, jfix,      /* indices of the fixed variable */
lambda[MAXN+1]; /* multipliers used in upper bounding */
struct NODE *next, *parent;   /* pointers (see below) */
} NODE, *NODEPTR;
The branch-and-bound tree consists of nodes as defined above. Each outstanding node is a
member of either or both of the two linked lists maintained for efficient operations:
1. Path from node to the root: Each node points to its parent through the *parent link, except
for the root node. The root's *parent is set to NULL indicating the end of a list traversal.
By following the path from a particular node to the root, the algorithm determines which
variables are fixed in the partial problem associated with this node. Another use of the
*parent pointer is to initialize the multiplier vector with the best multipliers computed
at the parent node.
2. Dangling nodes: This is a priority queue consisting only of dangling nodes, i.e., those without children. It is ordered by decreasing bound values. Nodes are connected via *next
links. The start of the queue is pointed by firstdangling, so that firstdangling-&gt;bound
always gives the highest upper bound among all yet-to-be-solved subproblems. When
firstdangling = NULL, the branch-and-bound algorithm is terminated because this condition implies there exists no upper bound greater than the incumbent (see BAB() function
for more information).
/* Set defaults for parameters */
/* (They can be modified by the use of configuration file kblgap.cfg) */


7



</P>
<P><PB REF="00000010.tif" SEQ="00000010" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="873" N="8">
int   INFINITY     = 32000,
ROOTMAMITLIM = 5,
MAMITLIM     = 5,
ROOTSUBITLIM = 20,
SUBITLIM     = 10,
SUBPATIENCE  = 4,
MAXBRANCH    = 10000,
PRINTSTEPS   = YES,
PRINTINPUT   = YES,
PRINTOPTIM   = YES;
float ZEROTOL = 0.001;
/* Global variables */


int m, n,                /* actual no of knapsack and MC constraints */
c[MAXM+1] [MAXN+1],  /* objective values */
a[MAXM+l] [MAXN+1],  /* knapsack weights */
b[MAXM+1],          /* knapsack capacities, or RHS values */
abslb,               /* sum of min objective values over every MC set */
nbranch = 0,         /* branch counter */
incumbent, xinc[MAXN+l1]; /* incumbent value and solution */


Feasible solutions handled in various routines are kept in n-dimensional vectors, called positions. Let x is a feasible solution to the GAP and pos is the corresponding position vector.
Then, for each j, pos [j] = i iff xij = 1. Incumbent solution xinc is a position vector.


NODEPTR firstdangling = NULL;


/* initially, there is no dangling nodes */


3.2 Input Functions
void readin(inpname)
char inpname[];
{
-FILE *inp;
int i, j;


if (inpname[O] == '*') {
printf("Enter input file: ");
}


scanf("%s", inpname);


if ((inp = fopen(inpname, "r")) == NULL) {
printf( "ERROR: Cannot open input file: %s\n", inpname);
exit(1);
}


8



</P>
<P><PB REF="00000011.tif" SEQ="00000011" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="777" N="9">
fscanf inp, "%d~d\n"I, &amp;m, &amp;n);
if (m &gt; MAXM) {
printf("ERROR: Row limit exceeded.")
printf (" (Data = %d, Limit %d) \n", m, MAXM);
exit (1)
}
if (n &gt; MAXN){
printf("IERROR: Column limit exceeded.")
printf (" (Data = %d, Limit %d)\n", n, MAXN);
exit (1)
}
for (i1l; i &lt;= m; i++){
for (j1; j&lt;=n; j++){
fscanf(inp, "/7.d"I, &amp;c[i][j]);
if (c[i][j] &lt; 0) {
printf("ERROR: Nonnegative objective value expected.");
printf(" (Data: cD'.d,%d] = %d)\n", i, j, c[iJ[jJ);
exit (1)
fscanf(inp, "\n");
for (j1; j&lt;=n; j++){
fscanf(inp, "U.", &amp;a[i][j]);
if (a[i][j] &lt;= 0) {
printf("ERROR: Positive knapsack weight expected.");
printf(" (Data: a[Yd,Yd] = '/d)\n", i, j, a[i][j]);
exit(1);
fscanf~inp, "\n");
for (i1l; i &lt;= m; i++) {
fscanf(inp, "7.", &amp;b[i]);
if (b~il &gt; MAXB) {
printf("ERROR: Knapsack RHS too big.");
printf(" (Data: b['hdl = '/d, Limit 'hd)\n", i, b~i], MAXB);
exit (1)
fscanf(inp, "\n");
fclose(inp);


9



</P>
<P><PB REF="00000012.tif" SEQ="00000012" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="728" N="10">
printf("PROBLEM DATA: Xd rows by %d columns", m, n);
printf("1 (Input file: /.s)\n\n", inpname);
if (PRINTINPUT) {
printf("Objective coefficients:\n");
f or (i1; i &lt;= m; i++) {
for (j1; j&lt;=n; j++) printf("1%3d", c~ilj]j);
printf("\n");
printf("\nKnapsack constraints:\n");
for (i1; i &lt;= m; i++){
for (j1; j&lt;n; j++) printf("%3d", a~iJ[j]);
printf (" &lt;= Xd\n", b [i]);
}
printf("\n");
}
} /* end of readin *
void setparams()
char iflag[3], bflag[3], of lag[3];
FILE *cfg;
mnt i, j;
char dummy[20];
if ((cfg = fopen("1kblgap.cfg", "r")) = NULL){
printf( "Cannot open kblgap-cfg, defaults are in effect.\n");
}


else {
fscanf (cfg,
fscanf (cfg,
fscanf (cfg,
fscanf (cfg,
fscanf (cfg,
fscanf (cfg,
fscanf (cfg,
fscanf (cfg,
fscanf (cfg,
fscanf(cfg,
fscanf (cfg,


1 1 44 d
"Yed
"Yed
"Yed
117.d
117ild
11 7od
11711d
"Yod
"Yod
Ilyof


Y.s\nll I
Y.s\nll I
Y.'s \ n I II
Y.s\nll I
Y.s\nllI
Y.s\nll.9
Y.s\nllI
Y.s\nll
Y.s\nll )
Y.s\nll
Y.s\nll j


&amp;INFINITY, dummy); 
&amp;ROOTMAMITLIM, d3ummy);_
&amp;MAMITLIM, dummy);
&amp;ROOTSUBITLIM, dummy);
&amp;SUBITLIM, dummy);
&amp;SUBPATIENCE, dummy);
&amp;MAXBRANCH, dummy);
&amp;PRINTSTEPS, dummy);
&amp;PRINTINPIJT, dummy);
&amp;PRINTOPTIM, dummy);
&amp;ZEROTOL, dummy);


f close (cf g);


}


10



</P>
<P><PB REF="00000013.tif" SEQ="00000013" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="766" N="11">
if ((ROOTMAMITLIM &lt;= 0 &amp;&amp; ROOTSUBITLIM &lt;= 0) II (MAMITLIM &lt;= 0 &amp;&amp; SUBITLIM &lt;= 0)){
printf("IERROR: One of iter limits for upper bounding must be positive.\n");
printf("I (Data: ROOTMAMITLIM = Xd, ROOTSUBITLIM =    dn
IROOTMAMITLIM, ROOTSUBITLIM);
printf("              MAMITLIM = %d,     SUBITLIM = %d)\n"., MAMITLIM, SUBITLIM);
exit (1)
if (PRINTINPUT) strcpy(iflag,"YES"); else strcpy(iflag,"NO")
if (PRINTSTEPS) strcpy(bflag,"YES"l); else strcpy(bflag,"NO")
if (PRINTOPTIM) strcpy(oflag,"YES"); else strcpy(oflag,"NO")


printf("\n --- PARAMETERS SET");
printf(" --- &mdash;----------------------&mdash; \n");
printf (


I I


MAM   SUB


PRINT FLAGS


printf (
11Root iter limit     7%3d   7/.3d     Input.
ROOTMAMITLIM, ROOTSUBITLIM, if lag, INFINITY);
printf (
"Node iter limit      7*3d   /.3d      Branches   %s
MAMITLIM, SUBITLIM, bf lag, ZEROTOL);
printf (
Patience             7%3d             Optimsol   %s
SUBPATIENCE, of lag, MAXBRANCH);
printf("~ ---    - - --  - - --  - --  - - --  - -
printf("-\n"); --- &mdash;----------&mdash; W
printf (
"Program Limits: Max Rows Yd, Max Columns 7.d, Max
MAXM, MAXN, MAXB);
printf("~ ---    - - --  - - --  - --  - - --  - -
printf("-\n"); --- &mdash;----------&mdash; W


GENERAL\n");
Infinity    %d\n",
Zero Tol    %6.4f\n",
Max Branch  %d\n",
Knapsack RHS Yd.dn",


}/* end of setparams *
3.3 Upper Bounding ]Functions
Two procedures are run in serial to obtain ani upper bound on the optimum objective of the
GAP or any of its subproblems obtained by fixing some of the variables. First, a multiplier
adjustment method (MAM) is called, followed by a, subgradient algorithm. Both procedures
solve a Lagrangian dual of the GAP for upper bounding. Associated Lagrangian relaxation is
obtained by dualizing multiple-choice constraints.
MAMO and subgradiento share a commoni input list:


*Forj'= 1,...,n
fI if x - =1I and xe,- = 0,f4 $i
jfixed~j]= ~0 if xi 1i ---,,.,x,- are all free
Let S ={Il'Ijf ixed[j]#:~0}.


11I



</P>
<P><PB REF="00000014.tif" SEQ="00000014" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="820" N="12">
* Sum of objective values of fixed variables


obj fixed =   Ck 3, k = if ixedlij]
3JES
* For i = 1...,Im
bbar[i] = b-    S   ak3, k =jfixedlj]
{a ESlk=i'}
* For j=1..,
Jlambda[j] =initial multiplier associated with multiple-choice constraint j.
The outputs are the vector of final multipliers and the function value itself that gives the
tightest upper bound obtained.
The following code for the MAM, described in detail in Karabakal, Bean, and Lohmann
(1992), is based on the idea of steepest descent improvements per iteration in solving the Lagrangian dual. Computationally, it outperforms its predecessors suggested by Fisher, Jaikumar,
and Van Wassenhove (1986) and Guignard and Rosenwein (1989).
jint MAM(jf ixed, objf ixed, bbar, lambda)
int j fixed[]  obj fixed, bbar[]l, lamnbda[]l
int iter, i, j, k, rhs, istar, jstar, lastjstar, firsttime, row,
p[MAXN+1], x[MAXM+1] [MAXN+11, pos [MAXN+1],
aknap[MAXN+1], xknap[MAXN+1], seq[MAXN+1],
dual, cknap[MAXN+1], vknap, slack[MAXN+1],
delta[MAXM+1] [MAXN+1], deltal, delta2,
imp, maximp, steplength, maxstep, bound;
bool optcomp;
void f bknapO   checkincumbent;
1* initialize'*
last'star = 0;
firsttime = YES;
iter = 0;
f* initialize dual *
for (j1, dual=0; j&lt;=n; j++) if (jfixed~j]==0)
dual += lambda~j];
while (1){
/* initialize column totals *
for (j1; j&lt;=n; j++) p[jl = 0;


12



</P>
<P><PB REF="00000015.tif" SEQ="00000015" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="786" N="13">
/* solve m forward-backward knapsacks *
for (i1; i&lt;=m; i++) {
k = 0; /* k counts no of elements in knapsack *
for (j1; j&lt;=n; j++)    if (jfixed[jJ =   0)
if (a[iII[jJ &lt;= bbar[i]) 
seq[++k] =
cknap [k] = c Ei] [jIi - lambda [jJ Iaknap [kJ a a[i] rjI]
for (j1; j&lt;=n; j++){
x[i][j]= 0; delta[i][j] = INFINITY;
}
if (k &gt; 0){
rhs = bbar[i];
fbknap(k, rhs, cknap, aknap, xknap, &amp;vknap, slack);
if (firsttime) dual += vknap;P
for (j=1; j&lt;=k; j++) {
if (xknap~j]) { x~i][seqlj]]     1; ++p[seq[j]]; }
delta[i] [seq[j]] = slack [ji;
} /* end of m forward-backward knapsacks *
bound = objfixed + dual;
if (f irsttime) {
firsttime = NO;
if (bound &lt;= incumbent) return(bound);
}
opt comp = YES;       /*assume optimum completion *
for (j1; j&lt;=n; j++) if (jfixed[j]=0)
if ((pEji     1)    optcomp = NO; break;}
if (optcomp){         /* optimum completion, compute the position vector *
f or (j=1; j &lt;n; j ++)  Pos[j I = j fixed EjJI
for (j1; -j&lt;=n; j++) if (jfixed[jl == 0)
for (i1l; i&lt;=m; i++)
if (x Ei][j]I == 1) { posEj]I = i; break; }
checkincumbent(pos);
return (bound);
}
else {/* not optimum completion *


13



</P>
<P><PB REF="00000016.tif" SEQ="00000016" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="807" N="14">
/* compute the steepest descent *
maximp = -INFINITY;
for (j1l; j&lt;=n; j++) if (jfixed[j] == 0)
if ((P[j]!= 1) &amp;&amp; (j = lastjstar)){
deltal = delta2 = INFINITY;
for (i1;- i&lt;=m; i++)
if ((p[j]==0) II ((p[j] &gt; 1) &amp;&amp; (x[i][jJ == 1))
if (delta[i] [ji &lt; deltal) {
delta2 =deltal; deltal = delta[i][jJ;
row=
else{
if (delta[i] [j] &lt; delta2) delta2= delta[i] [j];
}
if ((delta2!= INFINITY) &amp;&amp; (delta2 &gt; 0)){
imp = (p[j]==0)? deltal     (p[jJ-2) * delta2 + deltal;
if (imp &gt; maximp){
maximp =imp; maxstep =delta2;
jstar =j;    istar = row;   steplength = delta2;else
if ((imp == maximp) &amp;&amp; (delta2 &gt; maxstep)){
maxstep =delta2;
j star =;istar = row; steplength = delta2;
} /* end of for jloop/
}/* end of else not opt comp *


if (maximp == -INFINITY) return(bound); /* zero step length *
lastjstar = jstar;
if (p EjStar] == 0){
dual -= delta[istar] [jstar];
lambda~jstar] -= steplength;
}
else{
dual -     (p[jstarl-2) * steplength + delta~istar][jistar]l)
lambda~jstar] += steplength;
bound = objf ixed + dual;
if (bound &lt;= incumbent) return(bound);
else
if (++iter &gt; ((firstdangling == NULL)? ROOTMAMITLIM      MAMITLIM))
return (bound) 
}/* end of while (1) loop *
}/* end of MAM */


14



</P>
<P><PB REF="00000017.tif" SEQ="00000017" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="868" N="15">
MAM() calls fbknap() function to solve knapsacks with a post-optimality analysis, i.e., with
the calculation of the slack vector below.
Input:
* nknap = number of items in the knapsack problem,
* rhs = RHS capacity of the knapsack,
* cknap[j] = objective value of item j,
* aknap[j] = knapsack weight of item j,
Output
* Optimum solution
1 if item j is selected in the optimum
xknap[j]    0 otherwise
* vknap = optimum objective value,
* Slacks ("Aj"s)
minimum 6 such that cknap[j]+5 would cause  if xknap[j] = 0
xknap[j] = 1 in an alternative optimum
slack[j] =
minimum 6 such that cknap[j]-b would cause  if xknap[j] = 1
xknap[j] = 0 in an alternative optimum
See Karabakal, Bean, and Lohmann (1992) for the formulation of forward and backward
dynamic programming recursions and for the derivation of the slack vector.
void fbknap(nknap, rhs, cknap, aknap, xknap, vknap, slack)
int nknap, rhs, aknap[], xknap[], cknap[], *vknap, slack[];
{
int f[MAXN+1][MAXB], g[MAXN+] [MAXB];
int soln[MAXN+l] [MAXB], j, beta;
/* forward recursions */
for (beta=O; beta &lt; aknap[1]; beta++) {
f[1] [beta] = 0; soln[1] [beta] = 0;
}
for (beta=aknap[1]; beta &lt;= rhs; beta++)
if (cknap[1] &gt; 0) {
f[1][beta] = cknap[1];   soln[1] [beta] = 1;
}
else {
f[1] [beta] = 0; soln[l] [beta] = 0;
}


15



</P>
<P><PB REF="00000018.tif" SEQ="00000018" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="778" N="16">
for (j=2; j &lt;= nknap; j++) {
for (beta=O; beta &lt; aknap[j]; beta++){
f [j] [beta] = f Ej-1] [beta]; soln[j] [beta] = 0;
}
for (beta=aknap [j]; beta &lt;= rhs; beta++)
if (fU-1] [beta] &gt;= f[j-l1][beta-aknap[j]] + cknap[j]){
f[j] [beta] = f[j-1][beta]; soln[j][beta] = 0;
else{
f [j] [beta] = f [j-1] [beta-aknap[j]] + cknap[j];
soln[j] [beta] = 1;
}
/* determine optimum solution *
*vknap = f[nknap][rhs];
beta = rhs;       nknap;
while (j){
xknap[j] =soln[j] [beta];
if (soln [j]I [beta] ) beta -=aknap Ej]I
/* backward recursion *
f or (beta=0; beta&lt;=rhs; beta++)
g[nknap] [beta] = *vknap;
for (j~nknap-1; j &gt;= 1; j &mdash;){
for (beta~rhs; beta &gt;= rhs-aknap[j+1]+1; beta &mdash;)
g~j] [beta] = g[j+1] [beta];
for (beta~rhs-aknap~j+1]; beta &gt;= 0; beta &mdash;)
if (g~j+1] [beta] &lt; g[j+l][beta+aknap[j+1]]    cknap[j+l])
g[j] [beta] g[j+1] [beta];
else
g[j] [beta]l  g[j+1][be'ta+aknap~j+l]]   cknap[j+1];
}
/* compute slacks (,A3 quantities) *
slack[1] = INFINITY;
if (xknap[l] == 0) {
for (beta~aknap1l]; beta &lt;= rhs; beta++)
if (g[l] [beta]   cknap[1] &lt; slack[1])
slack[l] = g[1] [beta] - cknap[1];
}
else
for (beta=o; beta&lt;=rhs; beta++)
if (g [1] [beta] &lt;slack[l] ) slack[El] = E[l] [beta];


16



</P>
<P><PB REF="00000019.tif" SEQ="00000019" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="811" N="17">
for (j=2; j&lt;=nknap; j++){
slack[j] = INFINITY;
if (xknap[j] == 0) {
for (beta=aknap [j]; beta &lt;= rhs; beta++)
if (g [j]I[beta]  f f[j - 1][beta-aknap [j] I  cknap [j] I( slack [j])
slack[j] = g[j] [beta] - f [j-1] [beta-aknap[j]]- cknap[j];
}
else
for (beta=0;- beta &lt;= rhs; beta++)
if (g [j]I [beta]- f [j- 1] [beta] &lt; slack [j])
slack[j] = g~ji [beta] - f[j-1] [beta];
}/* end of fbknap/
The subgradiento( function implements the subgradient algorithm described in Fisher
(1981) for solving Lagrangian duals of integer programming problems. The following step size
is chosen for the GAP:
steps ize = u (L(A) - incumbent).  I- zmx.)
int subgradient(jfixed, objfixed, bbar, lambda)
int jfixed[], objfixed, bbar[];
float lambda[];
int iter = 0, badcount = 0, pos[MAXN+1],
p[MAXN+1], minp[MAXN+1], x[MAXM+1] [MAXN+1],
aknap[MAXN+1], xknap[MAXN+1], seq[MAXN+1],
i, j, k, rhs, bound;
float u, dual, mindual = INFINITY,
minlambda[MAXN+1], subgrad[MAXN+1],
norm, stepsize, cknap[MAXN+1], vknap;
bool optcomp;
void knapdpo, lowerboundo, checkincumbento;
if (firstdangling =  NULL) u = 2.0; else u = 1.0;
while (1) {
/* initialize dual *
for (j=1, dual=0; j&lt;=n; j++) if (jfixed[j]==0)
dual += lambda[j];
/* initialize column totals *
for (j1; j&lt;=n; j++) p~ji = 0;


I 7



</P>
<P><PB REF="00000020.tif" SEQ="00000020" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="803" N="18">
/* solve m. knapsacks */
for (=I; i&lt;=m; i++){
k = 0; /* k counts no of elements in knapsack *
for (j1; j&lt;=n; j++) if (jfixed[j] == 0)
if ( (a [i] [jJ &lt;= bbar [i] ) &amp;&amp; ( (c E[i][j]-lambda [j] I&gt; 0. 0)) 
seq[++k] =;
cknap[k]= c[i] [j] - lambda[j]; aknap[k] a=      ] j
}
for (j1; j&lt;=n; j++) x[i] [j] = 0;
if (k &gt; 0) {
rhs = bbar[i];
knapdp(k, rhs, cknap, aknap, xknap, &amp;vknap);
dual += vknap;
for (j=I. j&lt;=k; j++)
if (xknap[j]) { x[i][Eseq[j]] = 1; ++p~seq[j]]; }
}/* end of knapsack solutions *
bound = objfixed + (int) dual;


optcomp = YES;
for (j1l; j&lt;=n;
if ((p[j]!=


/*assume optimum completion */
j++) if (jfixed[j]0=)
1)) { optcomp = NO; break; }


if (optcomp) {        /* optimum completion, compute the position vector */
f or (j=1; j &lt;=n; j ++)  PosEj I = j fixed [j]I;
for (j1; j&lt;=n; j++) if (jfixed[jl == 0)
for (i1; i&lt;=m; i++)
if (x[i]E[ii == 1) { pos[jl = i; break; }
checkincumbent(pos);
return (bound);
}


else {-                 /* not optimui
lowerbound(jfixed, x, bbar, p);
if (bound &lt;= incumbent) 
return (bound);
else {              /* neither opti


rn completion */


imum completion nor inferior */


if (dual &lt; mindual) {                /* solution improved */
badcount = 0; mindual = dual;
for (j1; j&lt;=n; j++) {
minlambda[j] = lambda[j]; minp[j] =pj]
}


}
else {


/* solution did not improve */


if (++badcount == SUBPATIENCE) {
/* restore last best solution found and continue from there */
/* by halving the step size */


18



</P>
<P><PB REF="00000021.tif" SEQ="00000021" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="863" N="19">
badcount = O; dual = mindual;
for (j=1; j&lt;=n; j++) {
lambda[j] = minlambda[j]; p[j] = minp[j];
}
u = u / 2;
}
/* find a subgradient */
for (j=1; j&lt;=n; j++) if (jfixed[j] == 0)
subgrad[j] = 1 - p[j];
/* compute step size */
norm = 0;
for (j=1; j&lt;=n; j++) if (jfixed[j] == 0)
norm += subgrad[j] * subgrad[j];
stepsize = u * (objfixed + dual - incumbent) / norm;
if (stepsize &gt; ZEROTOL) {  /* update multipliers */
for (j=1; j&lt;=n; j++) if (jfixed[j] == O) {
lambda[j] -= stepsize * subgrad[j];
}
if (++iter &lt; ((firstdangling == NULL)? ROOTSUBITLIM: SUBITLIM)) {
continue;
/* either zero step size or iteration limit exceeded */
bound = objfixed + (int) mindual;
for (j=1; j&lt;=n; j++) lambda[j] = minlambda[j];
return(bound);
} /* else neither opt comp nor inferior */
3 /* else not opt comp */
/* while (1) 
) /* end of subgradient */
The knapdp() function, which the subgradient algorithm calls for optimum knapsack solutions, is a linked-list implementation of the "Procedure P2" given by Toth (1980). It is a forward
dynamic programming algorithm and uses the "further but cheaper"-type pruning technique to
improve performance. Note that, under this pruning, the slack vector cannot be calculated and
thus, this procedure is not suitable for solving the knapsack subproblems of the MAM(). Incidentally, the "Procedure P2" of Toth (1980) has a bug in it; it is corrected in the implementation
here.
The input-output parameters defined for function fbknap() are also valid for knapdp().


19



</P>
<P><PB REF="00000022.tif" SEQ="00000022" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="813" N="20">
void knapdp(nknap, rhs, cknap, aknap, xknap, vknap)
int nknap, rhs, aknap[], xknap[];
float cknap[], *vknap;
{
int L[MAXN+1][MAXB], val[MAXN+l] [MAXB], prev[MAXN+l][MAXB],
s[MAXN+l], h, k, j, y, xtemp, ptemp, mm;
float F[MAXN+1] [MAXB], ftemp;
/* initialize for mm = 1 */
L[l][O] = F[l][0] = val[l][0] = prev[i][0] = s[ll = 0;
if ( (aknap[l] &lt;= rhs) &amp;&amp; (cknap[l] &gt; 0).) {
L[l][1] = aknap[1]; F[] [1] = cknap[l];
val[l][1] = 1; prev[l][1] = O; s[ll = 1;
}
for (mm=2; mm &lt;= nknap; mm++) {
L[mm][O] = F[mm][O] = val[mm ][0] = prev[mm][0] = 0;
h = ( (s[mm-] == 0)? 0: 1); k = j = 0; y = aknap[mm];
while (1) {
if ( L[mm-l][h] &lt; y ) {
if ( F[mm-l][h] &gt; F[mm] [k  ) {
k++;
L[mm] [k] = L[mm-l][ h]; F[mm] [k] = F[mm-l] [h;
val[mm][k] = 0; prev[mm][k] = h;
}
if ( h == s[mm-1] ) break; else h++;
}
else {
if ( L[mm-l][h] &gt; y ) {
ftemp = F[mm-l] [j + cknap[mm];
if ( ftemp &gt; F[mm][k] ) {
k++;
L[mm][k] = y; F[mm][k] = ftemp;
val[mm][k] = 1; prev[mm][k] = j;
}
y = L[mm-1][++j] + aknap[mm];
}
else {  /* L[mm-l][h] = y*/
ftemp = F[mm-l][j] + cknap[mm];
xtemp = 1; ptemp = j;
if (F[mm-l][h] &gt; ftemp) {
ftemp = F[mm-l][h]; xtemp = 0; ptemp = h;
}
if (ftemp &gt; F[mm][k]) {
k++;
L[mm][k] = y; F[mm] [k  = ftemp;
val[mm][k] = xtemp; prev[mm][k] = ptemp;
}
y = L[mm-l][++j] + aknap[mm];
if (h == s[mm-1]) break; else h++;
}
}
} /* while (1) */


20



</P>
<P><PB REF="00000023.tif" SEQ="00000023" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="783" N="21">
/* step 3 */
while (1) {
if Cy &gt; rhs) break;
else {
f temp = Fllimm- 1][j]I + cknapli[mm];
if (ftemp &gt; F[nm]l[k]){
k++;
L[mmllk] = y; F[mm][k] = ftemp;
val[rnm][k] = 1; prevlmm][k] = j
if (j == S[mm-1]) break;
else
y = L[mm-l][++j] + aknap[rnm];
} *while (1) *
/* step 4 */
s[mm] = k;
} /* for (mm=2... *
1* trace links back to recover solution *
*vknap = F [nknap] [s[nknap]];
k = s [nknap];
for (mm=nknap; mm &gt;= 1; mm &mdash;){
xknap [mm] = val [mm] [k];
k = preyv[mm] [k];
}
}/* end of knapdp/
3.4 Lower Bounding Function (Heuristic)
Given a Lagrangian solution, x, that satisfies knapsack and integrality constraints, but violates multiple-choice constraints, the following heuristic attempts to obtain overall feasibility- by
modifying current assignments.
void lowerbound(jfixed, x, bbar, p)
mnt j fixed [], x [][MAXN+1], bb ar[]l, p ];
mnt pcopy[MAXN+1], xcopyEMAXM+1] [MAXN+1], capacity[MAXM+1],
pos [MAXN+1], aknap[MAXN+1], xknap[MAXN+1], seq[MAXN+1],
i, j, k, rhs, istar, feasible;
float minratio, temp. cknap[MAXN+1], vknap;
void knapdpQ), checkincumbent();
/* copy p to pcopy, x to xcopy *
for (j1; j&lt;=n; j++){
pcopylj] = pEji;
for (i=1; i&lt;=m; i++)
xcopy [ili [ = xli]E[ii
}


21



</P>
<P><PB REF="00000024.tif" SEQ="00000024" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="783" N="22">
/* compute available knapsack capacities */


for (i1l; i&lt;=m; i++) {
capacity Ei] = bbarEi];
for (j1; j&lt;=n; j++) if (jfixed[j] == 0)
if (xcopy[i] [ji == 1) capacity[i] -= a[i] [j];
/* Order the variables x, such that xi  I and p3- &gt; 1 in the current solution *
in ascending ci -/ai -. Following this order, set xij = 0 and increase the'slack
space of knapsack i by a l,. Repeat until p, ~ 1 for all j.
for (j1; j&lt;=n; j++) if (jfixed[j] == 0)
while (pcopy[j] &gt; 1) {
minratio = INFINITY;
for (i1; i&lt;=m; i++)
if (xcopyllij[j] == 1){
temp = (float) c~il [ji / (float) a[i] [j];
if (temp &lt; minratio){
minratio = temp; istar =;
xcopy[istarjlj] = 0; &mdash; pcopy[jl; capacity[istar] += a[istar] [jJ;
/* Using only variables XI~ with p3 0. solve all knapsack problems optimally *
for (i1; i&lt;=m; i++){
k = 0;                      /* k counts no of elements in knapsack *
for (j1; j&lt;=n; j++)    if (jfixed[j] == 0)
if ( (pcopy[j] = 0) &amp;&amp; (a[i] [j] &lt;= capacity[i])){
seq[++k] =
cknap [kl = c Ei][l [Iaknap [k], = a [i][Ej]I
if (k &gt; 0){
rhs = capacity[i];
knapdp(k, rhs, cknap, aknap, xknap, &amp;vknap);
for (j1; j&lt;=k; j++)
if (xknap Ej] == 1) {
xcopy[i] [seqEji] = 1; ++pcopy~seq~j]];
} /* end of knapsack solutions *


22



</P>
<P><PB REF="00000025.tif" SEQ="00000025" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="820" N="23">
feasible = 1;
for (j1; j&lt;=n; j++) if (jfixed[jJ == 0)
if (pcopy~jj!= 1) { feasible = 0; break; }
if (feasible) {
for (j=1; j&lt;=n; j++)  pos ji = jfixed[j];
for (j1; j&lt;=n; j++) if (jfixed[j] == 0)
for (i1; i&lt;=m; i++)
if (xcopyli][j] == 1) { pos[j] = i; break; }
checkincumbent(pos);
} /* end of lowerbound */
3.5 Fathoming Functions
Fathoming functions are designed to initialize parameters and organize calling sequences. They
return one if the node is fathomed, zero if the node is dangling. Fathomed nodes are cleared
from the memory immediately, whereas dangling nodes are inserted into a priority queue ordered
by upper bounds.
bool fathomroot()
NODEPTR root;
int jfixed[MAXN+1], i, j, maxi, max2, omin, omax, bound;
bool fathom, fathomrodeo;
void putdanglingo;
/* determine absolute lower and upper bounds */
absib = bound = 0;
for (j1; j&lt;=n; j++) {
cmin = INFINITY; cmax = -INFINITY;
for (i1; i&lt;=m; i++) {
if (c~i][j] &lt; cmin) cmin = c~i][i];
if (c[i] [ji &gt; cmax) cmax = c~il [ii;
absib += cmin; bound += cmax;
incumbent = absib;
/* create the root node */
if ((root = (NODEPTR) malloc(sizeof(NODE))) == NULL) {
printf( "Cannot create root node."); exit(1);
}
root-&gt;nodeno = 0;
root-&gt;parent = NULL;


23



</P>
<P><PB REF="00000026.tif" SEQ="00000026" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="807" N="24">
/* initialize multiplers by selecting the second maximum objective value *
/* of the variables from each multiple-choice set *
for (j1; j&lt;=n; j++){
maxl =max2 = -INFINITY;
for (i1; i&lt;=m; i++) if (a[i][jJ &lt;= b[i]){
if (c[i][j] &gt; mail) {
max2 = maxi;  maxi    c[iJ[j];
}
else
if (c~i][j] &gt; max2) max2 = c[iJ[j];
root-&gt;lambda[j] = (max2 == -INFINITY)? 0    max2;
if (PRINTSTEPS)
printf ("Node 0   D'.d, %.dJ =&gt; ", incumbent, bound);
for (j=l; j&lt;=n; j++) jfixed[j] = 0;   /* no variables are-fixed at the root/
fathom = fathomnode(root, jfixed);
if (!fathom) putdangling(root);
return (f athom);
}/* end of fathomroot *
bool fathomnode(bbnode, jfixed)
NODEPTR bbnode;
mnt jfixed[];
int objfixed, bbar[MAXM+l], bound, i, jmamiter, subiter,
ilambda[MAXN+l], 1UB;
float dlambda [MAXN+l];.bool fathom;
NODEPTR hold;
mnt MAMO, subgradientO);
void printsolO);
if (bbnode-&gt;nodeno){
++nbranch;
mamiter = MAMITLIM; subiter =SUBITLIM;
}
else{
mamiter = BROOTMAMITLIM; subiter = ROOTSUBITLIM;
}
/* add node's fixed var to jfixed *
if (bbnode-&gt;nodeno &gt; 0) jfixed~bbnode-&gt;jfix] = bbnode-&gt;if ix;


24



</P>
<P><PB REF="00000027.tif" SEQ="00000027" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="817" N="25">
for (P1; i&lt;=m; i++) bbar[i] = b~il;
objfixed = 0;
for (j=1; j&lt;=n; j++) if (jfixedlljj){
bbar[ jfixed~j] J =a[ jfixed~j] I[j];
objfixed += cE jfixedlij] ][jI;
}
for (j1; j&lt;=n; j++) ilambda[j] = bbnode-&gt;l~ambda[j];
if (mamiter &gt; 0)
bound = MAM(jfixed, objfixed, bbar, ilambda);
if (bound &gt; incumbent) {
for (j1; j&lt;=n; j++) dlambda[j] = (float) ilambda[j];
if (subiter &gt; 0)
bound = subgradient(jfixed, objfixed, bbar, dlambda);
if (bound &gt; incumbent) {
for (j1; j&lt;=n; j++) bbnode-&gt;lambda[j] = (int) dlambda[j];
bbnode-&gt;bound = bound;
fathom = (bound &lt;= incumbent)? YES:NO;
if (PRINTSTEPS) {
printf("[%.d,%d]  "  incumbent, bound);
if (fathom) printf("1FATHOM\n"); else printf("'DANGLING\n");
}
if(nbrarich &gt;= MAXBRANCH){
printf ("Branch limit exceeded.\n");
if (incumbent == abslb) {
printf("No feasible solution found.");
}
else{
printf("\nBest-solution found = 7.d,   ",incumbent);
if (bbnode-&gt;nodeno == 0) UB = bound;
else{
UB= (firstdangling == NULL)? -INFINITY      firstdangling-&gt;bound;
if (bbnode-&gt;ifix!= m){
hold = bbnode-&gt;parent;
if (hold-&gt;bound &gt; UB) UB = hold-&gt;bound;
printf("Upper bound = %d", UB);
printf("l (Error bound =42M         n
( 100 * (float) (UB- incumbent)/ (float) incumbent));
printf("No of branches =%d\n", nbranch);
if (PRINTOPTIM) printsol(xinc);
exit (0)


25



</P>
<P><PB REF="00000028.tif" SEQ="00000028" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="880" N="26">
/* correct jfixed */
if (bbnode-&gt;nodeno &gt; 0) jfixed[bbnode-&gt;jfix] = 0;
return(fathom);
} /* end of fathomnode */
3.6 Branch-and-Bound Function
A node selected from the list of dangling nodes must have a solution violating one or more
multiple-choice sets. At the beginning, the only dangling node is the root. Select a violated
multiple-choice set with largest multiplier. Since any feasible solution includes exactly one out of
m variables from this constraint, create m branches (Bean 1984). At each branch, one variable
is fixed at one, and the rest are automatically set to zero.
void BAB()
{
NODEPTR cnode, kid, search, getdangling();
int jfixed[MAXN+1], used[MAXM+1], i, j, jstar,
maxlambda, serialno = 0;
bool feasible, fathomnode();
void putdangling(, checkincumbent();
while (firstdangling!= NULL) {
cnode = getdangling();
/* determine fixed and free vars */
for (j=l; j&lt;=n; j++) jfixed[j] = 0;
search = cnode;
while (search-&gt;parent!= NULL) {
jfixed[ search-&gt;jfix ] = search-&gt;ifix;
search = search-&gt;parent;
}
/* select jstar = multiple choice set to branch (one having biggest multiplier) */
maxlambda = -INFINITY; jstar = 0;
for (j=l; j&lt;=n; j++) if (jfixed[j] == 0)
if (cnode-&gt;lambda[j] &gt;= maxlambda) {
maxlambda = cnode-&gt;lambda[j];
jstar = j;
}
/* determine used knapsack capacities by fixed variables */
for (i=l; i&lt;=m; i++) used[i] = 0;
for (j=1; j&lt;=n; j++) if (jfixed[j] &gt; 0)
used[jfixed[j]] += a[jfixed[j]][j];


26



</P>
<P><PB REF="00000029.tif" SEQ="00000029" RES="600dpi" FMT="TIFF6.0" FTR="UNSPEC" CNF="824" N="27">
if (jstar == 0) { /* hit the bottom, jfixed is nonzero
feasible = YES;
for (i1; i&lt;=m; i++)
if (used~il &gt; b[iJ) { feasible = NO; break; }
if (feasible) checkincumbent~jfixed);
else                          /* create m branches *
for (i1; i&lt;=m; i++)
if ((used[i]+a[i][jstar] &lt;= b[i]) &amp;&amp; cnode-&gt;bound &gt; incumbent){
if ((kid = (NODEPTR) malloc(sizeof(NODE))) == NULL){
printf( "Cannot create new branches.");
exit (1)
kid-&gt;nodeno = ++serialno;
kid-&gt;Parent = cnode;
kid-&gt;ifix = i; kid-&gt;jfix = jstar;
for (j=1; j&lt;=n; j++)
kid-&gt;1ambda[j] = cnode-&gt;lambda[j];
if (PRINTSTEPS)
printf ("Node Yd From %d: x(%d,%d) [%d,%d] ==&gt; "
kid-&gt;nodeno, cnode-&gt;nodeno, i, jstar,
incumbent, cnode-&gt;bound);
if (fathomriode(kid,jfixed))
f ree ((char *) kid);
else                       /* node dangling */
putdangling(kid);       /* insert into queue */
}/* end of while */
}/* end of BAB */
1. The function putdangling() puts a new node into the correct place so that the order of
decreasing upper bounds is maintained.
void putdangling(newnode)
NODEPTR newnode;
{
NODEPTR search, last;
newnode-&gt;next = NULL;
search = firstdangling;
if (search == NULL) firstdangling = newnode;
else {
while (search!= NULL)
if (search-&gt;bound &gt; newnode-&gt;bound) {
last = search;
search = search-&gt;next;
}
else break;
newnode-&gt;next = search;
if (search-&gt;nodeno == firstdangling-&gt;nodeno)
firstdangling = newnode;
else
last-&gt;next = newnode;
}
} /* end of putdangling */
2. The function getdangling() gets a dangling node with highest upper bound.
NODEPTR getdangling()
{
NODEPTR hold = firstdangling;
firstdangling = firstdangling-&gt;next;
return(hold);
} /* end of getdangling */
void removeinferiorso0
NODEPTR last, prey;
while (1)
if (firstdangling == NULL) return;
else {
last = f irstdangling; preyv NULL;
while (last-&gt;next!= NULL){
preyv last; last = last-&gt;next;
if (last-&gt;bound &lt;= incumbent) { /* delete last */
free((char *)last);
if (prey == NULL) {/* last was the only one */
firstdangling =NULL; return;
}
else
Prev-&gt;next = NULL;
else return;
} /* end of removeinferiors */
void printsol(pos)
mnt posE];
mnt i, j, used[MAXM+1], cx=0;
for (i1l; i&lt;=m; i++) usedEil = 0;
for (j1; j&lt;=n; j++){
i = pos~ji;  used~il += a~i]Ej]; cx +=ciE;
printf("\n cx = %d\n    ",'cx);
for (j1; j&lt;=n; iji-i) printf("%ld", (j % 10));
Printf("  USED  RHS\n");
for (i1; i&lt;=m; i++){
Printf ("7.2d)", i);
for (j1; j&lt;=n; j++)
if (pos[jJ == i) printf("1"); else printf("l.");
printf("I %4d 7.3d\n", used~il, b~i]);
}
} /* end of printsol*/
void checkincumbent(pos)
int posE];
int j, vpos = 0;
void removeinferiorso;
for (j1;  j&lt;=n; j++)  vpos += c~pos~ji] jl;
if (vpos &gt; incumbent) {
incumbent = vpos;
for (j1l; j&lt;=n; j++) xinc[j] = pos[j];
removeinferiorso;
}
} /* end of checkincumbent */
void main(argc, argv)
int argc;
char *argv[j;
char inpname[50];
void readino, BABO, setparamso, printsol();
bool fathomrootO;
setparams();
if (argc == 2)
strcpy(inpname, *++argv);
else
inpname[0] =
readin(inpname);
/* Solve the root problem; if duality gap, continue with branch-and bound */
if (!fathomrooto) BAB();
printf("\nOptimum = %d\nNo of branches = %d\n",
incumbent, nbranch);
if (PRINTOPTIM) printsol(xinc);
} /* end of main */

