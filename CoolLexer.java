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
    int comentarios = 0;
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
	private final int INSIDE_STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 3;
	private final int ERROR_STRING = 2;
	private final int INLINE_COMMENT = 4;
	private final int yy_state_dtrans[] = {
		0,
		72,
		102,
		108,
		111
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
		/* 51 */ YY_NO_ANCHOR,
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
		/* 72 */ YY_NOT_ACCEPT,
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
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NOT_ACCEPT,
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
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"65,62:7,57,58,60,56,57,59,62:18,56,62,61,62:5,22,23,33,32,25,29,36,34,55:10" +
",24,26,28,30,31,62,27,37,38,39,40,41,11,38,42,43,38:2,44,38,45,46,47,38,48," +
"49,10,50,51,52,38:3,62,63,62:2,53,62,3,64,1,16,8,19,54,7,5,54:2,2,54,6,13,1" +
"4,54,9,4,17,18,15,12,54:3,20,62,21,35,62,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,186,
"0,1,2,3,1:2,4,1:5,5,6,7,1:2,8,1:3,9,1,10,1,11,12,13,12,1:6,12:7,13,12:7,1:3" +
",14,1:7,15,1,15,1:6,16,1,17,18,19,20,13,12,13:8,12,13:5,21,22,23,24,25,26,2" +
"7,28,29,30,31,32,33,15,34,35,36,37,38,39,40,41,16,42,43,44,45,46,47,48,49,5" +
"0,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,7" +
"5,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,1" +
"00,101,12,13,102,103,104,105,106,107,108,109,110")[0];

	private int yy_nxt[][] = unpackFromString(111,66,
"1,2,135,175:2,73,137,175,177,175,3,74,179,96,181,175:2,183,175,100,4,5,6,7," +
"8,9,10,11,12,13,14,15,16,17,18,19,20,176:2,178,176,180,176,97,136,138,101,1" +
"82,176:4,184,15,175,21,22:4,23,24,15:2,175,15,-1:67,175,185,139,175:16,-1:1" +
"7,139,175:6,185,175:11,-1:8,175,-1:2,176:6,140,176:12,-1:17,176:5,140,176:1" +
"3,-1:8,176,-1:34,29,-1:61,30,31,-1:64,32,-1:67,33,-1:57,34,-1:97,21,-1:68,2" +
"3,-1:8,175:6,163,175:12,-1:17,175:5,163,175:13,-1:8,175,-1:2,175:19,-1:17,1" +
"75:19,-1:8,175,-1:2,176:19,-1:17,176:19,-1:8,176,-1:2,55:5,56,55:10,57,55,5" +
"8,55:37,-1:2,55,59,55:3,60,55,-1,105:58,-1:2,63,105:4,-1,114:58,-1,71,114:5" +
",1,50:5,92,50:53,51,52,50,53,50,54,-1,175:3,143,175,25,175:4,26,175:7,26,-1" +
":17,175:8,25,175:3,143,175:6,-1:8,175,-1:2,176:4,27,176:14,-1:17,176:6,27,1" +
"76:12,-1:8,176,-1:2,176:6,162,176:12,-1:17,176:5,162,176:13,-1:8,176,-1:19," +
"95,-1:48,105:58,-1,64,63,105:4,-1:33,68,-1:34,99,-1:64,175:10,28,175:7,28,-" +
"1:17,175:19,-1:8,175,-1:2,176:3,150,176,75,176:4,76,176:7,76,-1:17,176:8,75" +
",176:3,150,176:6,-1:8,176,-1:24,69,-1:44,54,-1:64,175:2,157,175,77,175:14,-" +
"1:17,157,175:5,77,175:12,-1:8,175,-1:2,176:10,78,176:7,78,-1:17,176:19,-1:8" +
",176,-1,1,61:58,-1,62,63,61,93,61:2,-1,175:9,35,175:6,35,175:2,-1:17,175:19" +
",-1:8,175,-1:2,176:9,79,176:6,79,176:2,-1:17,176:19,-1:8,176,-1:2,175:11,36" +
",175:7,-1:17,175:15,36,175:3,-1:8,175,-1:2,176:11,80,176:7,-1:17,176:15,80," +
"176:3,-1:8,176,-1,1,65:21,94,65:10,98,65:22,66:4,67,65:5,-1,175:9,37,175:6," +
"37,175:2,-1:17,175:19,-1:8,175,-1:2,176:9,81,176:6,81,176:2,-1:17,176:19,-1" +
":8,176,-1,1,70:58,-1,71,70:5,-1,175:7,38,175:11,-1:17,175:4,38,175:14,-1:8," +
"175,-1:2,176:5,42,176:13,-1:17,176:8,42,176:10,-1:8,176,-1:2,175:13,39,175:" +
"5,-1:17,175:10,39,175:8,-1:8,175,-1:2,176:7,82,176:11,-1:17,176:4,82,176:14" +
",-1:8,176,-1:2,175:7,40,175:11,-1:17,175:4,40,175:14,-1:8,175,-1:2,176:7,84" +
",176:11,-1:17,176:4,84,176:14,-1:8,176,-1:2,41,175:18,-1:17,175:2,41,175:16" +
",-1:8,175,-1:2,85,176:18,-1:17,176:2,85,176:16,-1:8,176,-1:2,175,43,175:17," +
"-1:17,175:7,43,175:11,-1:8,175,-1:2,176:13,83,176:5,-1:17,176:10,83,176:8,-" +
"1:8,176,-1:2,175:5,86,175:13,-1:17,175:8,86,175:10,-1:8,175,-1:2,176,87,176" +
":17,-1:17,176:7,87,176:11,-1:8,176,-1:2,175:7,44,175:11,-1:17,175:4,44,175:" +
"14,-1:8,175,-1:2,176:3,88,176:15,-1:17,176:12,88,176:6,-1:8,176,-1:2,175:3," +
"45,175:15,-1:17,175:12,45,175:6,-1:8,175,-1:2,176:7,89,176:11,-1:17,176:4,8" +
"9,176:14,-1:8,176,-1:2,175:7,46,175:11,-1:17,175:4,46,175:14,-1:8,175,-1:2," +
"176:15,90,176:3,-1:17,176:3,90,176:15,-1:8,176,-1:2,175:7,47,175:11,-1:17,1" +
"75:4,47,175:14,-1:8,175,-1:2,176:3,91,176:15,-1:17,176:12,91,176:6,-1:8,176" +
",-1:2,175:15,48,175:3,-1:17,175:3,48,175:15,-1:8,175,-1:2,175:3,49,175:15,-" +
"1:17,175:12,49,175:6,-1:8,175,-1:2,175:7,103,175:4,141,175:6,-1:17,175:4,10" +
"3,175:4,141,175:9,-1:8,175,-1:2,176:7,104,176:4,152,176:6,-1:17,176:4,104,1" +
"76:4,152,176:9,-1:8,176,-1:2,175:7,106,175:4,109,175:6,-1:17,175:4,106,175:" +
"4,109,175:9,-1:8,175,-1:2,176:7,107,176:4,110,176:6,-1:17,176:4,107,176:4,1" +
"10,176:9,-1:8,176,-1:2,175:3,112,175:15,-1:17,175:12,112,175:6,-1:8,175,-1:" +
"2,176:7,113,176:11,-1:17,176:4,113,176:14,-1:8,176,-1:2,175:12,115,175:6,-1" +
":17,175:9,115,175:9,-1:8,175,-1:2,176:2,158,176:16,-1:17,158,176:18,-1:8,17" +
"6,-1:2,175:14,161,175:4,-1:17,175:14,161,175:4,-1:8,175,-1:2,176:3,116,176:" +
"15,-1:17,176:12,116,176:6,-1:8,176,-1:2,175:3,117,175:15,-1:17,175:12,117,1" +
"75:6,-1:8,175,-1:2,176:3,118,176:15,-1:17,176:12,118,176:6,-1:8,176,-1:2,17" +
"5:2,119,175:16,-1:17,119,175:18,-1:8,175,-1:2,176:2,120,176:16,-1:17,120,17" +
"6:18,-1:8,176,-1:2,175:4,165,175:14,-1:17,175:6,165,175:12,-1:8,175,-1:2,17" +
"6:14,160,176:4,-1:17,176:14,160,176:4,-1:8,176,-1:2,175:12,121,175:6,-1:17," +
"175:9,121,175:9,-1:8,175,-1:2,176:12,122,176:6,-1:17,176:9,122,176:9,-1:8,1" +
"76,-1:2,175:7,123,175:11,-1:17,175:4,123,175:14,-1:8,175,-1:2,176:12,124,17" +
"6:6,-1:17,176:9,124,176:9,-1:8,176,-1:2,175:17,125,175,-1:17,175:13,125,175" +
":5,-1:8,175,-1:2,176:4,164,176:14,-1:17,176:6,164,176:12,-1:8,176,-1:2,175," +
"167,175:17,-1:17,175:7,167,175:11,-1:8,175,-1:2,176:3,126,176:15,-1:17,176:" +
"12,126,176:6,-1:8,176,-1:2,175:3,127,175:15,-1:17,175:12,127,175:6,-1:8,175" +
",-1:2,176:12,166,176:6,-1:17,176:9,166,176:9,-1:8,176,-1:2,175:12,169,175:6" +
",-1:17,175:9,169,175:9,-1:8,175,-1:2,176:7,168,176:11,-1:17,176:4,168,176:1" +
"4,-1:8,176,-1:2,175:7,171,175:11,-1:17,175:4,171,175:14,-1:8,175,-1:2,176,1" +
"28,176:17,-1:17,176:7,128,176:11,-1:8,176,-1:2,175,129,175:17,-1:17,175:7,1" +
"29,175:11,-1:8,175,-1:2,176:4,130,176:14,-1:17,176:6,130,176:12,-1:8,176,-1" +
":2,175:3,131,175:15,-1:17,175:12,131,175:6,-1:8,175,-1:2,176:8,170,176:10,-" +
"1:17,176:11,170,176:7,-1:8,176,-1:2,175:4,133,175:14,-1:17,175:6,133,175:12" +
",-1:8,175,-1:2,176:4,172,176:14,-1:17,176:6,172,176:12,-1:8,176,-1:2,175:8," +
"173,175:10,-1:17,175:11,173,175:7,-1:8,175,-1:2,176:9,132,176:6,132,176:2,-" +
"1:17,176:19,-1:8,176,-1:2,175:4,174,175:14,-1:17,175:6,174,175:12,-1:8,175," +
"-1:2,175:9,134,175:6,134,175:2,-1:17,175:19,-1:8,175,-1:2,175,145,175,147,1" +
"75:15,-1:17,175:7,145,175:4,147,175:6,-1:8,175,-1:2,176,142,144,176:16,-1:1" +
"7,144,176:6,142,176:11,-1:8,176,-1:2,175:6,149,175:12,-1:17,175:5,149,175:1" +
"3,-1:8,175,-1:2,176,146,176,148,176:15,-1:17,176:7,146,176:4,148,176:6,-1:8" +
",176,-1:2,175:12,151,175:6,-1:17,175:9,151,175:9,-1:8,175,-1:2,176:12,154,1" +
"76:6,-1:17,176:9,154,176:9,-1:8,176,-1:2,175:6,153,175,155,175:10,-1:17,175" +
":5,153,175:5,155,175:7,-1:8,175,-1:2,176:6,156,176:12,-1:17,176:5,156,176:1" +
"3,-1:8,176,-1:2,175:2,159,175:16,-1:17,159,175:18,-1:8,175,-1");

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
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
    */
   case INSIDE_STRING:
		{ 
		  	yybegin(YYINITIAL); 
		  	return new Symbol(TokenConstants.ERROR, "EOF in string constant"); 
		}
	case COMMENT:
		{ 
			yybegin(YYINITIAL); 
			return new Symbol(TokenConstants.ERROR, "EOF in comment");
		}
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
						
					case -2:
						break;
					case 2:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -3:
						break;
					case 3:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -4:
						break;
					case 4:
						{ /* Simbolos y operadores */
											  return new Symbol(TokenConstants.LBRACE); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.COLON);  }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.COMMA);  }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.SEMI);   }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.AT);     }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.LT);}
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.MINUS);}
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.EQ);}
					case -15:
						break;
					case 15:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  yybegin(YYINITIAL);
                                  return new Symbol(TokenConstants.ERROR, yytext());
                                  }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.PLUS);}
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.MULT);}
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.DIV);}
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.NEG);}
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.DOT);}
					case -21:
						break;
					case 21:
						{
	/* Constante entera */
	return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); 
}
					case -22:
						break;
					case 22:
						{/* Espacio en blanco*/ }
					case -23:
						break;
					case 23:
						{
	curr_lineno++; 
}
					case -24:
						break;
					case 24:
						{
	/* Limpiamos nuestro buffer y cambiamos de estado */
	string_buf.setLength(0); 
	yybegin(INSIDE_STRING); 
}
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.IN);	}
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.IF); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.FI); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.OF); }
					case -29:
						break;
					case 29:
						{ 
	/* Comentario multilenea*/
	comentarios++;
	yybegin(COMMENT);	
}
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.LE);}
					case -32:
						break;
					case 32:
						{
	yybegin(INLINE_COMMENT); 
}
					case -33:
						break;
					case 33:
						{ /* Sample lexical rule for "=>" arrow.
					                                     Further lexical rules should be defined
					                                     here, after the last %% separator */
					                                  return new Symbol(TokenConstants.DARROW); }
					case -34:
						break;
					case 34:
						{
	/*Comentario extra, mostrar error */
	return new Symbol(TokenConstants.ERROR, "Unmatched *)"); 
}
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LET); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NEW); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NOT); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.CASE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.LOOP); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ELSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.ESAC); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.THEN); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.POOL); }
					case -44:
						break;
					case 44:
						{ /* Booleans */
											  return new Symbol(TokenConstants.BOOL_CONST, "true"); }
					case -45:
						break;
					case 45:
						{ /* Palabras reservadas */
											  return new Symbol(TokenConstants.CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.WHILE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.BOOL_CONST, "false"); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{
	/*Se agrega cualquier texto*/
	if (string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append(yytext()); 
	} 	else { 
			yybegin(ERROR_STRING); 
			return new Symbol(TokenConstants.ERROR, "String constant too long"); 
	}
}
					case -51:
						break;
					case 51:
						{
	  curr_lineno++;
	  yybegin(YYINITIAL);
	  return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); 
  }
					case -52:
						break;
					case 52:
						{
	/*Se regresa al estado principal*/
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
}
					case -53:
						break;
					case 53:
						{ /* no hacer nada*/ }
					case -54:
						break;
					case 54:
						{ 
	yybegin(ERROR_STRING);
	return new Symbol(TokenConstants.ERROR, "String contains null character.");
}
					case -55:
						break;
					case 55:
						{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append(yytext().charAt(1));
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -56:
						break;
					case 56:
						{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\n");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -57:
						break;
					case 57:
						{ 	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\t");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -58:
						break;
					case 58:
						{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\f");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -59:
						break;
					case 59:
						{	
	curr_lineno++;
	if( string_buf.length() < MAX_STR_CONST-1 )
	 {
		 string_buf.append('\n');
	 } else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -60:
						break;
					case 60:
						{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\b");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -61:
						break;
					case 61:
						{ }
					case -62:
						break;
					case 62:
						{	
	curr_lineno++;
	yybegin(YYINITIAL);
}
					case -63:
						break;
					case 63:
						{
	 yybegin(YYINITIAL); 
	}
					case -64:
						break;
					case 64:
						{ 
	curr_lineno++; 
}
					case -65:
						break;
					case 65:
						{ /* No hacer nada */ }
					case -66:
						break;
					case 66:
						{ /* Espacios en blanco*/ }
					case -67:
						break;
					case 67:
						{ curr_lineno++; }
					case -68:
						break;
					case 68:
						{ comentarios++; }
					case -69:
						break;
					case 69:
						{ 	/* Estado para comentarios multilinea */
	comentarios--;
	if (comentarios == 0)  
	{   
		yybegin(YYINITIAL); 
	}
}
					case -70:
						break;
					case 70:
						{ /* No hacer nada */ }
					case -71:
						break;
					case 71:
						{ /* Estado para comentarios de linea */
  curr_lineno++; 
  yybegin(YYINITIAL);
}
					case -72:
						break;
					case 73:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -73:
						break;
					case 74:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.IN);	}
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.IF); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.FI); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.OF); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.LET); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.NEW); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.NOT); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.CASE); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.LOOP); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.ELSE); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.ESAC); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.THEN); }
					case -86:
						break;
					case 87:
						{ return new Symbol(TokenConstants.POOL); }
					case -87:
						break;
					case 88:
						{ /* Palabras reservadas */
											  return new Symbol(TokenConstants.CLASS); }
					case -88:
						break;
					case 89:
						{ return new Symbol(TokenConstants.WHILE); }
					case -89:
						break;
					case 90:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -90:
						break;
					case 91:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -91:
						break;
					case 92:
						{
	/*Se agrega cualquier texto*/
	if (string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append(yytext()); 
	} 	else { 
			yybegin(ERROR_STRING); 
			return new Symbol(TokenConstants.ERROR, "String constant too long"); 
	}
}
					case -92:
						break;
					case 93:
						{ }
					case -93:
						break;
					case 94:
						{ /* No hacer nada */ }
					case -94:
						break;
					case 96:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -95:
						break;
					case 97:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -96:
						break;
					case 98:
						{ /* No hacer nada */ }
					case -97:
						break;
					case 100:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -98:
						break;
					case 101:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -99:
						break;
					case 103:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -100:
						break;
					case 104:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -101:
						break;
					case 106:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -102:
						break;
					case 107:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -103:
						break;
					case 109:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -104:
						break;
					case 110:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -105:
						break;
					case 112:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -106:
						break;
					case 113:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -107:
						break;
					case 115:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -108:
						break;
					case 116:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -109:
						break;
					case 117:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -110:
						break;
					case 118:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -111:
						break;
					case 119:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -112:
						break;
					case 120:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -113:
						break;
					case 121:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -114:
						break;
					case 122:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -115:
						break;
					case 123:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -116:
						break;
					case 124:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -117:
						break;
					case 125:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -118:
						break;
					case 126:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -119:
						break;
					case 127:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -120:
						break;
					case 128:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -121:
						break;
					case 129:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -122:
						break;
					case 130:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -123:
						break;
					case 131:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -124:
						break;
					case 132:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -125:
						break;
					case 133:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -126:
						break;
					case 134:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -127:
						break;
					case 135:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -128:
						break;
					case 136:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -129:
						break;
					case 137:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -130:
						break;
					case 138:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -131:
						break;
					case 139:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -132:
						break;
					case 140:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -133:
						break;
					case 141:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -134:
						break;
					case 142:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -135:
						break;
					case 143:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -136:
						break;
					case 144:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -137:
						break;
					case 145:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -138:
						break;
					case 146:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -139:
						break;
					case 147:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -140:
						break;
					case 148:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -141:
						break;
					case 149:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -142:
						break;
					case 150:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -143:
						break;
					case 151:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -144:
						break;
					case 152:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -145:
						break;
					case 153:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -146:
						break;
					case 154:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -147:
						break;
					case 155:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -148:
						break;
					case 156:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -149:
						break;
					case 157:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -150:
						break;
					case 158:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -151:
						break;
					case 159:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -152:
						break;
					case 160:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -153:
						break;
					case 161:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -154:
						break;
					case 162:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -155:
						break;
					case 163:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -156:
						break;
					case 164:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -157:
						break;
					case 165:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -158:
						break;
					case 166:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -159:
						break;
					case 167:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -160:
						break;
					case 168:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -161:
						break;
					case 169:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -162:
						break;
					case 170:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -163:
						break;
					case 171:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -164:
						break;
					case 172:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -165:
						break;
					case 173:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -166:
						break;
					case 174:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -167:
						break;
					case 175:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -168:
						break;
					case 176:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -169:
						break;
					case 177:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -170:
						break;
					case 178:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -171:
						break;
					case 179:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -172:
						break;
					case 180:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -173:
						break;
					case 181:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -174:
						break;
					case 182:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -175:
						break;
					case 183:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -176:
						break;
					case 184:
						{
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -177:
						break;
					case 185:
						{
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}
					case -178:
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
