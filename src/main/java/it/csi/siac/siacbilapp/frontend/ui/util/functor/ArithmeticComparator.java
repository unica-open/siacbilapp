/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.functor;

import java.math.BigDecimal;

/**
 * Comparatore per i controlli aritmetici.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/giu/2014
 *
 */
public enum ArithmeticComparator implements BinaryOperator<BigDecimal, BigDecimal, Boolean> {
	
	/** Checks whether the first argument is greater than the second */
	GREATER {
		@Override
		public Boolean performOperation(BigDecimal left, BigDecimal right) {
			return Boolean.valueOf(left.compareTo(right) > 0);
		}
	},
	/** Checks whether the first argument is greater than the second or equals to it */
	GREATER_OR_EQUAL {
		@Override
		public Boolean performOperation(BigDecimal left, BigDecimal right) {
			return Boolean.valueOf(left.compareTo(right) >= 0);
		}
	},
	/** Checks whether the first argument is lesser than the second */
	LESSER {
		@Override
		public Boolean performOperation(BigDecimal left, BigDecimal right) {
			return Boolean.valueOf(left.compareTo(right) < 0);
		}
	},
	/** Checks whether the first argument is lesser than the second or equals to it */
	LESSER_OR_EQUAL {
		@Override
		public Boolean performOperation(BigDecimal left, BigDecimal right) {
			return Boolean.valueOf(left.compareTo(right) <= 0);
		}
	},
	/** Checks whether the first argument is equals to the second */
	EQUAL {
		@Override
		public Boolean performOperation(BigDecimal left, BigDecimal right) {
			return Boolean.valueOf(left.compareTo(right) == 0);
		}
	},
	;
	
}
