package jp.bj_one.re.exembed;

import java.util.Locale;

import jp.bj_one.re.exembed.ExDirective.Type;

class ExEmbedDirective {
	static final String START_KEY = "BJ{";
	static final String END_KEY = "}";
	static final String START_ARRAY = "[";
	static final String END_ARRAY = "]";
	static final char COMMENT_CHAR = '!';

	/** "BJ{ XX[i] CC }" 開始位置. */
	final int start;

	/** 終了位置. */
	final int end;

	/** "XX[i]" 部分. */
	String findWord;

	/** "CC" 部分.コメント. */
	String comment;
	
	/** "XX" 部分.NOT_DIRECTIVE の場合は START_KEY 部分. */
	String directive;

	/** 配列・List型の添え字. -1 の場合は添え字無し */
	int index;

	ExDirective exDirective = null;
	
	protected ExEmbedDirective(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	enum FoundType {
		NOT_FOUND,
		NOT_DIRECTIVE,
		IS_COMMENT,
		IS_BASIC_TYPE,
		IS_ARRAY_TYPE,
		IS_NO_INDEX_ARRAY_TYPE,
		Illegal_DIRECTIVE,
		Illegal_INDEX
	};
	
	FoundType type;
	
	public boolean isError() {
		return
			type == FoundType.NOT_FOUND ||
			type == FoundType.Illegal_DIRECTIVE ||
			type == FoundType.Illegal_INDEX;
	}

	/**
	 * BJ{ の検索.
	 * 
	 * @param source
	 */
	public static ExEmbedDirective findDirective(String source) {
		StartEndKeyword f = findKeyword(source, START_KEY, END_KEY);
		if (f == null) {
			ExEmbedDirective r = new ExEmbedDirective(-1, -1);
			r.type = FoundType.NOT_FOUND;
			return r;
		}
		
		ExEmbedDirective result = new ExEmbedDirective(f.start, f.end);
		if (f.keyword.isEmpty()) {
			result.type = FoundType.NOT_DIRECTIVE;
			result.directive = source.substring(f.start, f.start + START_KEY.length());
			return result;
		}
		
		if (f.keyword.charAt(0) == COMMENT_CHAR) {
			result.type = FoundType.IS_COMMENT;
			result.directive = "";
			result.comment = f.keyword.substring(1);
			return result;
		}
		
		int i = f.keyword.indexOf(' ');
		if (i >= 0) {
			result.findWord = f.keyword.substring(0, i);
			result.comment = f.keyword.substring(i).trim();
		} else {
			result.findWord = f.keyword;
			result.comment = "";
		}
		
		f = findKeyword(result.findWord, START_ARRAY, END_ARRAY);
		if (f == null) {
			result.directive = result.findWord;
			result.type = FoundType.IS_BASIC_TYPE;
			result.exDirective = new ExDirective(result.directive, Type.BASIC_TYPE, -1);
		} else {
			result.directive = result.findWord.substring(0, f.start).trim();
			if (f.keyword.isEmpty()) {
				result.type = FoundType.IS_NO_INDEX_ARRAY_TYPE;
				result.index = -1;
				result.exDirective = new ExDirective(result.directive, Type.NO_INDEX_ARRAY_TYPE, -1);
			} else {
				try {
					result.index = Integer.parseInt(f.keyword);
				} catch (NumberFormatException e) {
					// index が数値ではない構文エラー
					result.type = FoundType.Illegal_INDEX;
					return result;
				}
				if (result.index < 0) {
					// index が負数の構文エラー
					result.type = FoundType.Illegal_INDEX;
					return result;
				}
				result.type = FoundType.IS_ARRAY_TYPE;
				result.exDirective = new ExDirective(result.directive, Type.ARRAY_TYPE, result.index);
			}
		}
		
		if (!isAvailableKeyword(result.directive))
			result.type = FoundType.Illegal_DIRECTIVE;
		
		return result;
	}

	protected static class StartEndKeyword {
		final int start; // 含まれる
		final int end; // 含まれない (String.substring() と同じ)
		final String keyword;

		public StartEndKeyword(int start, int end, String keyword) {
			this.start = start;
			this.end = end;
			this.keyword = keyword;
		}
	}

	/**
	 * 
	 * @param source
	 * @param start
	 * @param end
	 * @return nullの場合は見つからなかった。見つかったら見つかった場所、中のキーワードを返す。
	 */
	protected static StartEndKeyword findKeyword(String source, String start, String end) {
		if (source == null || source.isEmpty())
			return null;
		if (start == null || start.isEmpty())
			return null;
		if (end == null || end.isEmpty())
			return null;

		final Locale locale = Locale.US;
		String upperd = source.toUpperCase(locale);
		int startIdx = upperd.indexOf(start.toUpperCase(locale));
		if (startIdx < 0)
			return null;
		int endIdx = upperd.substring(startIdx + start.length()).indexOf(end.toUpperCase(locale));
		if (endIdx < 0)
			return null;
		endIdx += startIdx + start.length() + end.length();

		String keyword = source.substring(startIdx + start.length(), endIdx - end.length()).trim();

		return new StartEndKeyword(startIdx, endIdx, keyword);
	}
	
	protected static boolean isAvailableKeyword(String word) {
		if (word == null || word.isEmpty())
			return false;
		
		final String[] javaKeywords = {
			    "abstract","continue","for",       "new",      "switch",
			    "assert",  "default", "if",        "package",  "synchronized",
			    "boolean", "do",      "goto",      "private",  "this",
			    "break",   "double",  "implements","protected","throw",
			    "byte",    "else",    "import",    "public",   "throws",
			    "case",    "enum",    "instanceof","return",   "transient",
			    "catch",   "extends", "int",       "short",    "try",
			    "char",    "final",   "interface", "static",   "void",
			    "class",   "finally", "long",      "strictfp", "volatile",
			    "const",   "float",   "native",    "super",    "while",
			    "_"
		};
		for (String javaKeyword : javaKeywords) {
			if (javaKeyword.equals(word))
				return false;
		}
		
		if (!Character.isJavaIdentifierStart(word.codePointAt(0)))
			return false;
		for (int i = word.offsetByCodePoints(0, 1); i < word.length(); i = word.offsetByCodePoints(i, 1)) {
			if (!Character.isJavaIdentifierPart(word.codePointAt(i)))
				return false;
		}
		
		return true;
	}
}
