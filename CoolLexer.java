/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
    String currentString = "";
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		51
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"61:9,4,3,61:2,0,61:18,7,61,58,61:5,8,9,18,13,16,6,15,19,60,57:9,12,14,5,1,2" +
",61:2,39,40,41,42,43,27,40,44,45,40:2,46,40,47,48,49,40,50,51,22,52,53,54,4" +
"0:3,61,59,61:2,55,61,33,56,32,35,23,36,56,28,26,56:2,24,56,20,21,30,56,29,2" +
"5,37,38,34,31,56:3,10,61,11,17,61,62:2")[0];

	private int yy_rmap[] = unpackFromString(1,155,
"0,1,2:3,3,2:14,4,5,6,2:5,7,8,7,9,7:3,9,7:11,2:4,10,11,12,9,13,9,7,9:3,7,9:9" +
",14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38" +
",39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63" +
",64,65,66,67,68,69,70,7,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87," +
"88,89,90,91,9,92,93,94,95")[0];

	private int yy_nxt[][] = unpackFromString(96,63,
"-1,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,52,21,117,126,128,72," +
"53,128:2,130,132,134,128:3,74,136,128,150:2,151,150,152,150,73,118,127,75,1" +
"53,150:4,154,2,128,22,23,2,22,2,24,-1:2,25,-1:124,26,-1:4,27,-1:76,128,76,1" +
"28,78,128:19,78,128:4,76,128:9,-1:2,128,-1:22,150:8,129,150:15,129,150:13,-" +
"1:2,150,-1:59,22,-1:2,22,-1:22,128:38,-1:2,128,-1:22,128:8,123,128:15,123,1" +
"28:13,-1:2,128,-1:22,150:38,-1:2,150,-1:3,47:2,48,47:54,49,71,47:2,24,-1:20" +
",128:7,28,128:8,28,128:21,-1:2,128,-1:22,150:6,31,150:18,31,150:12,-1:2,150" +
",-1:22,150:8,143,150:15,143,150:13,-1:2,150,-1:62,50,-1:22,29,128:4,88,128," +
"30,128:8,30,128:10,29,128:3,88,128:6,-1:2,128,-1:22,55,150:4,138,150,56,150" +
":8,56,150:10,55,150:3,138,150:6,-1:2,150,-1:22,128:6,57,128:6,92,128:5,92,1" +
"28:5,57,128:12,-1:2,128,-1:22,150:7,54,150:8,54,150:21,-1:2,150,-1:22,128:2" +
",32,128:14,32,128:20,-1:2,128,-1:22,150:2,60,150:14,60,150:20,-1:2,150,-1:2" +
"2,128:11,33,128:22,33,128:3,-1:2,128,-1:22,150:2,58,150:14,58,150:20,-1:2,1" +
"50,-1:22,128:5,98,128:25,98,128:6,-1:2,128,-1:22,150:11,59,150:22,59,150:3," +
"-1:2,150,-1:22,128:13,100,128:5,100,128:18,-1:2,128,-1:22,35,150:26,35,150:" +
"10,-1:2,150,-1:22,128,102,128:26,102,128:9,-1:2,128,-1:22,150:3,66,150:19,6" +
"6,150:14,-1:2,150,-1:22,128:2,34,128:14,34,128:20,-1:2,128,-1:22,150:3,62,1" +
"50:19,62,150:14,-1:2,150,-1:22,128:14,103,128:18,103,128:4,-1:2,128,-1:22,1" +
"50:12,63,150:8,63,150:16,-1:2,150,-1:22,128:6,122,128:18,122,128:12,-1:2,12" +
"8,-1:22,150:10,64,150:18,64,150:8,-1:2,150,-1:22,128:4,124,128:21,124,128:1" +
"1,-1:2,128,-1:22,150:4,65,150:21,65,150:11,-1:2,150,-1:22,128:3,107,128:19," +
"107,128:14,-1:2,128,-1:22,150:5,68,150:25,68,150:6,-1:2,150,-1:22,128:18,10" +
"8,128:13,108,128:5,-1:2,128,-1:22,150:3,67,150:19,67,150:14,-1:2,150,-1:22," +
"128:3,36,128:19,36,128:14,-1:2,128,-1:22,150:15,69,150:6,69,150:15,-1:2,150" +
",-1:22,128:12,37,128:8,37,128:16,-1:2,128,-1:22,150:5,70,150:25,70,150:6,-1" +
":2,150,-1:22,128:10,38,128:18,38,128:8,-1:2,128,-1:22,128,110,128:26,110,12" +
"8:9,-1:2,128,-1:22,128:4,39,128:21,39,128:11,-1:2,128,-1:22,128:5,112,128:2" +
"5,112,128:6,-1:2,128,-1:22,128:3,40,128:19,40,128:14,-1:2,128,-1:22,61,128:" +
"26,61,128:10,-1:2,128,-1:22,128:3,41,128:19,41,128:14,-1:2,128,-1:22,128:9," +
"125,128:20,125,128:7,-1:2,128,-1:22,128:6,114,128:18,114,128:12,-1:2,128,-1" +
":22,128:3,42,128:19,42,128:14,-1:2,128,-1:22,128:5,43,128:25,43,128:6,-1:2," +
"128,-1:22,128:3,44,128:19,44,128:14,-1:2,128,-1:22,128:15,45,128:6,45,128:1" +
"5,-1:2,128,-1:22,128:2,116,128:14,116,128:20,-1:2,128,-1:22,128:5,46,128:25" +
",46,128:6,-1:2,128,-1:22,128:4,80,82,128:20,80,128:4,82,128:6,-1:2,128,-1:2" +
"2,150,139,150,77,150:19,77,150:4,139,150:9,-1:2,150,-1:22,128:5,106,128:25," +
"106,128:6,-1:2,128,-1:22,128:13,105,128:5,105,128:18,-1:2,128,-1:22,128,104" +
",128:26,104,128:9,-1:2,128,-1:22,128:4,111,128:21,111,128:11,-1:2,128,-1:22" +
",128:3,109,128:19,109,128:14,-1:2,128,-1:22,128:5,113,128:25,113,128:6,-1:2" +
",128,-1:22,128:6,115,128:18,115,128:12,-1:2,128,-1:22,128,84,128,86,128:19," +
"86,128:4,84,128:9,-1:2,128,-1:22,150,79,150,81,150:19,81,150:4,79,150:9,-1:" +
"2,150,-1:22,150:3,83,150:19,83,150:14,-1:2,150,-1:22,128,121,128:26,121,128" +
":9,-1:2,128,-1:22,150:13,142,150:5,142,150:18,-1:2,150,-1:22,128:8,90,128:1" +
"5,90,128:13,-1:2,128,-1:22,150:5,85,150:25,85,150:6,-1:2,150,-1:22,128:4,12" +
"0,128:8,119,128:5,119,128:6,120,128:11,-1:2,128,-1:22,150:5,87,150:25,87,15" +
"0:6,-1:2,150,-1:22,128:8,94,96,128:14,94,128:5,96,128:7,-1:2,128,-1:22,150:" +
"13,89,150:5,89,150:18,-1:2,150,-1:22,150:14,144,150:18,144,150:4,-1:2,150,-" +
"1:22,150,91,150:26,91,150:9,-1:2,150,-1:22,150,93,150:26,93,150:9,-1:2,150," +
"-1:22,150:6,145,150:18,145,150:12,-1:2,150,-1:22,150:5,95,150:25,95,150:6,-" +
"1:2,150,-1:22,150:3,146,150:19,146,150:14,-1:2,150,-1:22,150,147,150:26,147" +
",150:9,-1:2,150,-1:22,150:4,97,150:21,97,150:11,-1:2,150,-1:22,150:9,148,15" +
"0:20,148,150:7,-1:2,150,-1:22,150:6,99,150:18,99,150:12,-1:2,150,-1:22,150:" +
"6,149,150:18,149,150:12,-1:2,150,-1:22,150:2,101,150:14,101,150:20,-1:2,150" +
",-1:22,150:4,131,150:8,133,150:5,133,150:6,131,150:11,-1:2,150,-1:22,150:4," +
"135,137,150:20,135,150:4,137,150:6,-1:2,150,-1:22,150,140,150:26,140,150:9," +
"-1:2,150,-1:22,150:8,141,150:15,141,150:13,-1:2,150,-1:2");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
    case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -2:
						break;
					case 2:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -3:
						break;
					case 3:
						{
    curr_lineno++;
}
					case -4:
						break;
					case 4:
						{
}
					case -5:
						break;
					case 5:
						{
    return new Symbol(TokenConstants.LT);
}
					case -6:
						break;
					case 6:
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -7:
						break;
					case 7:
						{
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.LPAREN);
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.RPAREN);
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.LBRACE);
}
					case -11:
						break;
					case 11:
						{
    return new Symbol(TokenConstants.RBRACE);
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -13:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -14:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -15:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -16:
						break;
					case 16:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -17:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.NEG);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -20:
						break;
					case 20:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -21:
						break;
					case 21:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -22:
						break;
					case 22:
						{
    AbstractSymbol numero = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, numero);
}
					case -23:
						break;
					case 23:
						{
    currentString = "";
    yybegin(STRING);
}
					case -24:
						break;
					case 24:
						
					case -25:
						break;
					case 25:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -26:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.LE);
}
					case -27:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -28:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.OF);
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.IN);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.IF);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.FI);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.LET);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -36:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.BOOL_CONST, true);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.BOOL_CONST, false);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -47:
						break;
					case 47:
						{
    currentString += yytext();
    if(currentString.length() > MAX_STR_CONST){
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
}
					case -48:
						break;
					case 48:
						{
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -49:
						break;
					case 49:
						{
    AbstractSymbol str =AbstractTable.stringtable.addString(currentString);
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.STR_CONST, str);
}
					case -50:
						break;
					case 50:
						{
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -51:
						break;
					case 52:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -52:
						break;
					case 53:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -53:
						break;
					case 54:
						{
    return new Symbol(TokenConstants.OF);
}
					case -54:
						break;
					case 55:
						{
    return new Symbol(TokenConstants.IN);
}
					case -55:
						break;
					case 56:
						{
    return new Symbol(TokenConstants.IF);
}
					case -56:
						break;
					case 57:
						{
    return new Symbol(TokenConstants.FI);
}
					case -57:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -58:
						break;
					case 59:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -59:
						break;
					case 60:
						{
    return new Symbol(TokenConstants.LET);
}
					case -60:
						break;
					case 61:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -61:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -62:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -63:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -64:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -65:
						break;
					case 66:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -66:
						break;
					case 67:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -70:
						break;
					case 71:
						{
    currentString += yytext();
    if(currentString.length() > MAX_STR_CONST){
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
}
					case -71:
						break;
					case 72:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -72:
						break;
					case 73:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -73:
						break;
					case 74:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -74:
						break;
					case 75:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -75:
						break;
					case 76:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -76:
						break;
					case 77:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -77:
						break;
					case 78:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -78:
						break;
					case 79:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -79:
						break;
					case 80:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -80:
						break;
					case 81:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -81:
						break;
					case 82:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -82:
						break;
					case 83:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -83:
						break;
					case 84:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -84:
						break;
					case 85:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -85:
						break;
					case 86:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -86:
						break;
					case 87:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -87:
						break;
					case 88:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -88:
						break;
					case 89:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -89:
						break;
					case 90:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -90:
						break;
					case 91:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -91:
						break;
					case 92:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -92:
						break;
					case 93:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -93:
						break;
					case 94:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -94:
						break;
					case 95:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -95:
						break;
					case 96:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -96:
						break;
					case 97:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -97:
						break;
					case 98:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -98:
						break;
					case 99:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -99:
						break;
					case 100:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -100:
						break;
					case 101:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -101:
						break;
					case 102:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -102:
						break;
					case 103:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -103:
						break;
					case 104:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -104:
						break;
					case 105:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -105:
						break;
					case 106:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -106:
						break;
					case 107:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -107:
						break;
					case 108:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -108:
						break;
					case 109:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -109:
						break;
					case 110:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -110:
						break;
					case 111:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -111:
						break;
					case 112:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -112:
						break;
					case 113:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -113:
						break;
					case 114:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -114:
						break;
					case 115:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -115:
						break;
					case 116:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -116:
						break;
					case 117:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -117:
						break;
					case 118:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -118:
						break;
					case 119:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -119:
						break;
					case 120:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -120:
						break;
					case 121:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -121:
						break;
					case 122:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -122:
						break;
					case 123:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -123:
						break;
					case 124:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -124:
						break;
					case 125:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -125:
						break;
					case 126:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -126:
						break;
					case 127:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -127:
						break;
					case 128:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -128:
						break;
					case 129:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -129:
						break;
					case 130:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -130:
						break;
					case 131:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -131:
						break;
					case 132:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -132:
						break;
					case 133:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -133:
						break;
					case 134:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -134:
						break;
					case 135:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -135:
						break;
					case 136:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}
					case -136:
						break;
					case 137:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -137:
						break;
					case 138:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -138:
						break;
					case 139:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -139:
						break;
					case 140:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -140:
						break;
					case 141:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -141:
						break;
					case 142:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -142:
						break;
					case 143:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -143:
						break;
					case 144:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -144:
						break;
					case 145:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -145:
						break;
					case 146:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -146:
						break;
					case 147:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -147:
						break;
					case 148:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -148:
						break;
					case 149:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -149:
						break;
					case 150:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -150:
						break;
					case 151:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -151:
						break;
					case 152:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -152:
						break;
					case 153:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -153:
						break;
					case 154:
						{
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}
					case -154:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
