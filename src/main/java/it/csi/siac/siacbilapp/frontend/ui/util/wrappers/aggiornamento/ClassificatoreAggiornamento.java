/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Wrapper astratto per porre in sessione gli elementi da verificare durante l'aggiornamento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 *
 */
public abstract class ClassificatoreAggiornamento implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7441196793778629128L;

	private ElementoPianoDeiConti elementoPianoDeiConti;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	private TipoFinanziamento tipoFinanziamento;
	private TipoFondo tipoFondo;
	private ClassificatoreGenerico classificatoreGenerico1;
	private ClassificatoreGenerico classificatoreGenerico2;
	private ClassificatoreGenerico classificatoreGenerico3;
	private ClassificatoreGenerico classificatoreGenerico4;
	private ClassificatoreGenerico classificatoreGenerico5;
	private ClassificatoreGenerico classificatoreGenerico6;
	private ClassificatoreGenerico classificatoreGenerico7;
	private ClassificatoreGenerico classificatoreGenerico8;
	private ClassificatoreGenerico classificatoreGenerico9;
	private ClassificatoreGenerico classificatoreGenerico10;
	private ClassificatoreGenerico classificatoreGenerico11;
	private ClassificatoreGenerico classificatoreGenerico12;
	private ClassificatoreGenerico classificatoreGenerico13;
	private ClassificatoreGenerico classificatoreGenerico14;
	private ClassificatoreGenerico classificatoreGenerico15;

	/**
	 * @return the elementoPianoDeiConti
	 */
	public ElementoPianoDeiConti getElementoPianoDeiConti() {
		return elementoPianoDeiConti;
	}

	/**
	 * @param elementoPianoDeiConti the elementoPianoDeiConti to set
	 */
	public void setElementoPianoDeiConti(ElementoPianoDeiConti elementoPianoDeiConti) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the tipoFinanziamento
	 */
	public TipoFinanziamento getTipoFinanziamento() {
		return tipoFinanziamento;
	}

	/**
	 * @param tipoFinanziamento the tipoFinanziamento to set
	 */
	public void setTipoFinanziamento(TipoFinanziamento tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}

	/**
	 * @return the tipoFondo
	 */
	public TipoFondo getTipoFondo() {
		return tipoFondo;
	}

	/**
	 * @param tipoFondo the tipoFondo to set
	 */
	public void setTipoFondo(TipoFondo tipoFondo) {
		this.tipoFondo = tipoFondo;
	}

	/**
	 * @return the classificatoreGenerico1
	 */
	public ClassificatoreGenerico getClassificatoreGenerico1() {
		return classificatoreGenerico1;
	}

	/**
	 * @param classificatoreGenerico1 the classificatoreGenerico1 to set
	 */
	public void setClassificatoreGenerico1(ClassificatoreGenerico classificatoreGenerico1) {
		this.classificatoreGenerico1 = classificatoreGenerico1;
	}

	/**
	 * @return the classificatoreGenerico2
	 */
	public ClassificatoreGenerico getClassificatoreGenerico2() {
		return classificatoreGenerico2;
	}

	/**
	 * @param classificatoreGenerico2 the classificatoreGenerico2 to set
	 */
	public void setClassificatoreGenerico2(ClassificatoreGenerico classificatoreGenerico2) {
		this.classificatoreGenerico2 = classificatoreGenerico2;
	}

	/**
	 * @return the classificatoreGenerico3
	 */
	public ClassificatoreGenerico getClassificatoreGenerico3() {
		return classificatoreGenerico3;
	}

	/**
	 * @param classificatoreGenerico3 the classificatoreGenerico3 to set
	 */
	public void setClassificatoreGenerico3(ClassificatoreGenerico classificatoreGenerico3) {
		this.classificatoreGenerico3 = classificatoreGenerico3;
	}

	/**
	 * @return the classificatoreGenerico4
	 */
	public ClassificatoreGenerico getClassificatoreGenerico4() {
		return classificatoreGenerico4;
	}

	/**
	 * @param classificatoreGenerico4 the classificatoreGenerico4 to set
	 */
	public void setClassificatoreGenerico4(ClassificatoreGenerico classificatoreGenerico4) {
		this.classificatoreGenerico4 = classificatoreGenerico4;
	}

	/**
	 * @return the classificatoreGenerico5
	 */
	public ClassificatoreGenerico getClassificatoreGenerico5() {
		return classificatoreGenerico5;
	}

	/**
	 * @param classificatoreGenerico5 the classificatoreGenerico5 to set
	 */
	public void setClassificatoreGenerico5(ClassificatoreGenerico classificatoreGenerico5) {
		this.classificatoreGenerico5 = classificatoreGenerico5;
	}

	/**
	 * @return the classificatoreGenerico6
	 */
	public ClassificatoreGenerico getClassificatoreGenerico6() {
		return classificatoreGenerico6;
	}

	/**
	 * @param classificatoreGenerico6 the classificatoreGenerico6 to set
	 */
	public void setClassificatoreGenerico6(ClassificatoreGenerico classificatoreGenerico6) {
		this.classificatoreGenerico6 = classificatoreGenerico6;
	}

	/**
	 * @return the classificatoreGenerico7
	 */
	public ClassificatoreGenerico getClassificatoreGenerico7() {
		return classificatoreGenerico7;
	}

	/**
	 * @param classificatoreGenerico7 the classificatoreGenerico7 to set
	 */
	public void setClassificatoreGenerico7(ClassificatoreGenerico classificatoreGenerico7) {
		this.classificatoreGenerico7 = classificatoreGenerico7;
	}

	/**
	 * @return the classificatoreGenerico8
	 */
	public ClassificatoreGenerico getClassificatoreGenerico8() {
		return classificatoreGenerico8;
	}

	/**
	 * @param classificatoreGenerico8 the classificatoreGenerico8 to set
	 */
	public void setClassificatoreGenerico8(ClassificatoreGenerico classificatoreGenerico8) {
		this.classificatoreGenerico8 = classificatoreGenerico8;
	}

	/**
	 * @return the classificatoreGenerico9
	 */
	public ClassificatoreGenerico getClassificatoreGenerico9() {
		return classificatoreGenerico9;
	}

	/**
	 * @param classificatoreGenerico9 the classificatoreGenerico9 to set
	 */
	public void setClassificatoreGenerico9(ClassificatoreGenerico classificatoreGenerico9) {
		this.classificatoreGenerico9 = classificatoreGenerico9;
	}

	/**
	 * @return the classificatoreGenerico10
	 */
	public ClassificatoreGenerico getClassificatoreGenerico10() {
		return classificatoreGenerico10;
	}

	/**
	 * @param classificatoreGenerico10 the classificatoreGenerico10 to set
	 */
	public void setClassificatoreGenerico10(ClassificatoreGenerico classificatoreGenerico10) {
		this.classificatoreGenerico10 = classificatoreGenerico10;
	}
	
	/**
	 * @return the classificatoreGenerico11
	 */
	public ClassificatoreGenerico getClassificatoreGenerico11() {
		return classificatoreGenerico11;
	}

	/**
	 * @param classificatoreGenerico11 the classificatoreGenerico11 to set
	 */
	public void setClassificatoreGenerico11(ClassificatoreGenerico classificatoreGenerico11) {
		this.classificatoreGenerico11 = classificatoreGenerico11;
	}

	/**
	 * @return the classificatoreGenerico12
	 */
	public ClassificatoreGenerico getClassificatoreGenerico12() {
		return classificatoreGenerico12;
	}

	/**
	 * @param classificatoreGenerico12 the classificatoreGenerico12 to set
	 */
	public void setClassificatoreGenerico12(ClassificatoreGenerico classificatoreGenerico12) {
		this.classificatoreGenerico12 = classificatoreGenerico12;
	}

	/**
	 * @return the classificatoreGenerico13
	 */
	public ClassificatoreGenerico getClassificatoreGenerico13() {
		return classificatoreGenerico13;
	}

	/**
	 * @param classificatoreGenerico13 the classificatoreGenerico13 to set
	 */
	public void setClassificatoreGenerico13(ClassificatoreGenerico classificatoreGenerico13) {
		this.classificatoreGenerico13 = classificatoreGenerico13;
	}

	/**
	 * @return the classificatoreGenerico14
	 */
	public ClassificatoreGenerico getClassificatoreGenerico14() {
		return classificatoreGenerico14;
	}

	/**
	 * @param classificatoreGenerico14 the classificatoreGenerico14 to set
	 */
	public void setClassificatoreGenerico14(ClassificatoreGenerico classificatoreGenerico14) {
		this.classificatoreGenerico14 = classificatoreGenerico14;
	}

	/**
	 * @return the classificatoreGenerico15
	 */
	public ClassificatoreGenerico getClassificatoreGenerico15() {
		return classificatoreGenerico15;
	}

	/**
	 * @param classificatoreGenerico15 the classificatoreGenerico15 to set
	 */
	public void setClassificatoreGenerico15(ClassificatoreGenerico classificatoreGenerico15) {
		this.classificatoreGenerico15 = classificatoreGenerico15;
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder
			.append("elemento del piano dei conti", elementoPianoDeiConti)
			.append("struttura amministrativo contabile", strutturaAmministrativoContabile)
			.append("tipo finanziamento", tipoFinanziamento)
			.append("tipo fondo", tipoFondo)
			.append("classificatore generico 1", classificatoreGenerico1)
			.append("classificatore generico 2", classificatoreGenerico2)
			.append("classificatore generico 3", classificatoreGenerico3)
			.append("classificatore generico 4", classificatoreGenerico4)
			.append("classificatore generico 5", classificatoreGenerico5)
			.append("classificatore generico 6", classificatoreGenerico6)
			.append("classificatore generico 7", classificatoreGenerico7)
			.append("classificatore generico 8", classificatoreGenerico8)
			.append("classificatore generico 9", classificatoreGenerico9)
			.append("classificatore generico 10", classificatoreGenerico10);
		return toStringBuilder.toString();
	}
	
}
